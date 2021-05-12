package com.kedacom.flinketlgraph.accumulator;

import com.kedacom.flinketlgraph.transform.TransmetricState;
import org.apache.flink.api.common.accumulators.Accumulator;
import org.apache.flink.api.common.accumulators.SimpleAccumulator;

public class TransAccumulator implements SimpleAccumulator<TransmetricState> {
    private TransmetricState localvalue;

    public TransAccumulator(){
        localvalue = new TransmetricState();
    }

    public TransAccumulator(TransmetricState state){
        this.localvalue = state;
    }

    @Override
    public void add(TransmetricState transmetricState) {
        this.localvalue.AddTransmetricState(transmetricState);
    }

    @Override
    public TransmetricState getLocalValue() {
        return this.localvalue;
    }

    @Override
    public void resetLocal() {
        this.localvalue.clear();
    }

    @Override
    public void merge(Accumulator<TransmetricState, TransmetricState> accumulator) {
        this.add(accumulator.getLocalValue());
    }

    @Override
    public TransAccumulator clone() {
        TransAccumulator result = new TransAccumulator();
        result.add(this.getLocalValue());
        return result;
    }
}
