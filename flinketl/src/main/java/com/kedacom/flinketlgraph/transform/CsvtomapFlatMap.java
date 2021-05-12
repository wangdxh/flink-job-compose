package com.kedacom.flinketlgraph.transform;

import com.kedacom.flinketlgraph.CommonUtils;
import com.kedacom.flinketlgraph.accumulator.TransAccumulator;
import com.kedacom.flinketlgraph.json.Columninfo;
import com.kedacom.flinketlgraph.json.Csvtomapspec;
import com.kedacom.flinketlgraph.json.Graphnode;
import org.apache.flink.api.common.functions.InvalidTypesException;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.util.Collector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CsvtomapFlatMap extends RichFlatMapFunction<Object, Object> implements CheckpointedFunction {

    private static final long serialVersionUID = 4744381842859501540L;

    private  Csvtomapspec spec;
    private String splitstr;
    private List<Columninfo> columninfos;
    private transient TransAccumulator acc;
    private transient ListState<TransmetricState> checkpointStates;
    private Pattern quotemarkPatt = Pattern.compile("^\""+"(.*)"+"\"$");
    private Graphnode joboperator;

    public CsvtomapFlatMap(Graphnode trans) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        this.spec = mapper.convertValue(trans.getElementconfig(), Csvtomapspec.class);
        StringBuffer stringBuffer = new StringBuffer();
        this.splitstr = stringBuffer.append("\\s*")
                .append(spec.getSplitstr())
                .append("\\s*")
                .append("(?=([^\"]*\"[^\"]*\")*[^\"]*$)").toString();
        columninfos = spec.getColumninfo();
        joboperator = trans;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        if (acc==null){
            acc = new TransAccumulator();
        }
        this.getRuntimeContext().addAccumulator(joboperator.getElementname()+":"+joboperator.getNodeid(), acc);
    }

    @Override
    public void flatMap(Object value, Collector<Object> out) throws Exception {
        if (!(value instanceof String)){
            throw new InvalidTypesException("csvflatmap inputtype is error");
        }
        acc.getLocalValue().processAddCount(1);

        String line = (String) value;
        String[] split = line.split(splitstr, -1);
        if (split.length < columninfos.size()){
            acc.getLocalValue().update( 1, "after splitted this line, the number of cells less than coluninfos.size");
            return;
        }
        Map<String, Object> ret = new HashMap();
        try {
            for (Columninfo columninfo : columninfos){
                String colname = columninfo.getColname();
                int colindex = columninfo.getColindex().intValue();
                Columninfo.Coltype coltype = columninfo.getColtype();
                String cell = split[colindex];
                Matcher matcher = quotemarkPatt.matcher(cell);
                if (matcher.find()){
                    cell = matcher.group(1);
                }

                switch (coltype){
                    case STRING:
                        ret.put(colname, String.valueOf(cell));
                        break;
                    case INTEGER:
                        ret.put(colname, Integer.valueOf(cell));
                        break;
                    case LONG:
                        ret.put(colname, Long.valueOf(cell));
                        break;
                    case FLOAT:
                        ret.put(colname, Double.valueOf(cell));
                        break;
                }
            }
        } catch (Exception e) { // NumberFormatException
            acc.getLocalValue().update(1L, e.getMessage());
        }

        if (ret.size()==columninfos.size()){
            out.collect(ret);
        }
    }

    @Override
    public void snapshotState(FunctionSnapshotContext context) throws Exception {
        checkpointStates.clear();
        checkpointStates.add(acc.getLocalValue());
    }

    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        ListStateDescriptor<TransmetricState> descriptor =
                new ListStateDescriptor<>("csvtomaptransform", TransmetricState.class);
        checkpointStates = context.getOperatorStateStore().getListState(descriptor);
        if (context.isRestored()){
            acc = CommonUtils.restoreTransmetricState2(checkpointStates);
        }
    }

}
