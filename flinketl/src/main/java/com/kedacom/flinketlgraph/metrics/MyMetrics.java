package com.kedacom.flinketlgraph.metrics;

import com.codahale.metrics.SlidingTimeWindowReservoir;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.dropwizard.metrics.DropwizardHistogramWrapper;
import org.apache.flink.metrics.Counter;
import org.apache.flink.metrics.Gauge;
import org.apache.flink.metrics.Histogram;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MyMetrics {
    private transient Counter counter;
    private transient Gauge<Map<String, Integer>> gauge;
    private transient Map<String, Integer> exceptions;
    private transient Histogram histogram;

    public MyMetrics(RuntimeContext ctx, String strcounter, String strgauge, String strhistogram){

        if (strcounter!=null && strcounter.length()>0){
            counter = ctx.getMetricGroup().counter(strcounter);
        }

        if (strgauge!=null && strgauge.length()>0){
            exceptions = new HashMap<>();
            gauge = ctx.getMetricGroup().gauge(strgauge, new Gauge<Map<String, Integer>>() {
                @Override
                public Map<String, Integer> getValue() {
                    return exceptions;
                }
            });
        }

        if (strhistogram!=null && strhistogram.length()>0){
            com.codahale.metrics.Histogram dropwizardHistogram =
                    //new com.codahale.metrics.Histogram(new SlidingWindowReservoir(500));
                    new com.codahale.metrics.Histogram(new SlidingTimeWindowReservoir(10, TimeUnit.MINUTES));
            this.histogram = ctx.getMetricGroup()
                    .histogram(strhistogram, new DropwizardHistogramWrapper(dropwizardHistogram));
        }
    }

    public void addcount(int x){
        counter.inc(x);
    }
    public void addExcepton(String msg){
        exceptions.merge(msg, 1, Integer::sum);
    }
    public void updateHistogram(long x){
        this.histogram.update(x);
    }
}
