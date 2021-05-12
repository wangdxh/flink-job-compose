package com.kedacom.flinksqlfunction;

import org.apache.flink.table.functions.FunctionContext;
import org.apache.flink.table.functions.ScalarFunction;


public class MyUdfIntToString extends ScalarFunction {
    // 可选，open方法可以不写。
    // 如果编写open方法需要声明'import org.apache.flink.table.functions.FunctionContext;'。
    @Override
    public void open(FunctionContext context) {
    }

    public String eval(String a) {
        return a + "this is after process";
    }

    //可选，close方法可以不写。
    @Override
    public void close() {
    }
}