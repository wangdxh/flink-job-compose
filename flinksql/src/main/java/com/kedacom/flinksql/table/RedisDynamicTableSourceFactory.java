package com.kedacom.flinksql.table;

import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;
import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.table.connector.sink.DynamicTableSink;
import org.apache.flink.table.factories.DynamicTableSinkFactory;
import org.apache.flink.table.factories.FactoryUtil;

import java.util.HashSet;
import java.util.Set;

public class RedisDynamicTableSourceFactory implements DynamicTableSinkFactory {

    public static final ConfigOption<String> host = ConfigOptions.key("host").stringType().noDefaultValue();
    public static final ConfigOption<Integer> port = ConfigOptions.key("port").intType().noDefaultValue();

    @Override
    public DynamicTableSink createDynamicTableSink(Context context) {
        final FactoryUtil.TableFactoryHelper helper = FactoryUtil.createTableFactoryHelper(this, context);
        helper.validate();
        ReadableConfig options = helper.getOptions();
        return new RedisDynamicTableSink(options);
    }

    @Override
    public String factoryIdentifier() {
        return "redis";
    }

    @Override
    public Set<ConfigOption<?>> requiredOptions() {
        Set<ConfigOption<?>> options = new HashSet();
        options.add(host);
        return options;
    }

    @Override
    public Set<ConfigOption<?>> optionalOptions() {
        Set<ConfigOption<?>> options = new HashSet();
        options.add(port);
        return options;
    }
}