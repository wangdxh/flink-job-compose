package com.kedacom.flinketlgraph.accumulator;

import org.apache.flink.api.common.accumulators.Accumulator;
import org.apache.flink.api.common.accumulators.SimpleAccumulator;

public class JdbcSourceV2Accumulator  implements SimpleAccumulator<JdbcSourceV2State> {
    private JdbcSourceV2State localvalue;

    public JdbcSourceV2Accumulator(JdbcSourceV2State localvalue) {
        this.localvalue = localvalue;
    }

    public JdbcSourceV2Accumulator() {
        this.localvalue = new JdbcSourceV2State();
    }

    @Override
    public void add(JdbcSourceV2State jdbcSourceV2State) {
        this.localvalue = jdbcSourceV2State;
    }

    @Override
    public JdbcSourceV2State getLocalValue() {
        return localvalue;
    }

    @Override
    public void resetLocal() {
        this.localvalue.clear();
    }

    @Override
    public void merge(Accumulator<JdbcSourceV2State, JdbcSourceV2State> accumulator) {
        this.localvalue = accumulator.getLocalValue();
    }

    @Override
    public Accumulator<JdbcSourceV2State, JdbcSourceV2State> clone() {
        return new JdbcSourceV2Accumulator(
                new JdbcSourceV2State(
                    localvalue.getTotalnums(), localvalue.getProcessedNum(), localvalue.getUniquecolumn()));
    }
}
