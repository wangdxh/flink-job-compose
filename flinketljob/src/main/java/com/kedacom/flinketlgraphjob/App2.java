package com.kedacom.flinketlgraphjob;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;


public class App2
{
    public static void main1(String[] args) throws Exception
    {
        String x = "xxx";

        switch (x){
            case "xxxx":
                System.out.println("in case xxxx");

            case "xxx":
                System.out.println("in case xxx");

            default:{
                System.out.println("in default ");
            }
        }
    }

    public static void main(String[] args) throws Exception{
        final OutputTag<String> machineTag = new OutputTag<String>("machine"){};
        final OutputTag<String> zzzztag = new OutputTag<String>("zzzzz"){};
        final OutputTag<String> machineTag2 = new OutputTag<String>("machine"){};
        final OutputTag<String> zzzztag2 = new OutputTag<String>("zzzzz"){};

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        SingleOutputStreamOperator<String> sideout = env.fromElements(WORDS)
                .flatMap(new FlatMapFunction<String, String>() {
                    @Override
                    public void flatMap(String value, Collector<String> out) throws Exception {
                        String[] splits = value.toLowerCase().split("\\W+");

                        for (String split : splits) {
                            if (split.length() > 0) {
                                out.collect(split);
                            }
                        }
                    }
                }).process(new ProcessFunction<String, String>() {
            @Override
            public void processElement(String value, Context ctx, Collector<String> out) throws Exception {
                if (value.length()%2 == 0){
                    ctx.output(machineTag2, "in mach---" + value);
                }else{
                    ctx.output(zzzztag2, "in zzzz____" + value);
                }
                out.collect(value);
            }
        });
        sideout.print();
        sideout.getSideOutput(machineTag).print();
        sideout.getSideOutput(zzzztag).print();

        env.execute("testsideout");
    }

    private static final String[] WORDS = new String[]{
            "To be, or not to be,--that is the question:--",
            "Whether 'tis nobler in the mind to suffer",
            "The slings and arrows of outrageous fortune",
            "Or to take arms against a sea of troubles,",
            "And by opposing end them?--To die,--to sleep,--",
            "No more; and by a sleep to say we end",
            "The heartache, and the thousand natural shocks"
    };
}
