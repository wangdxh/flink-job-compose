package com.kedacom.flinksql.format.protobuf;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;
import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.formats.json.JsonOptions;
import org.apache.flink.formats.json.JsonRowDataSerializationSchema;
import org.apache.flink.formats.json.TimestampFormat;
import org.apache.flink.table.api.ValidationException;
import org.apache.flink.table.connector.ChangelogMode;
import org.apache.flink.table.connector.format.DecodingFormat;
import org.apache.flink.table.connector.format.EncodingFormat;
import org.apache.flink.table.connector.sink.DynamicTableSink;
import org.apache.flink.table.connector.source.DynamicTableSource;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.factories.DeserializationFormatFactory;
import org.apache.flink.table.factories.DynamicTableFactory;
import org.apache.flink.table.factories.FactoryUtil;
import org.apache.flink.table.factories.SerializationFormatFactory;
import org.apache.flink.table.types.DataType;
import org.apache.flink.table.types.logical.RowType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ProtobufFormatFactory implements
        DeserializationFormatFactory,
        SerializationFormatFactory {

    public static final String IDENTIFIER = "protobuf";

    public static final ConfigOption<Boolean> FAIL_ON_MISSING_FIELD = ConfigOptions
            .key("fail-on-missing-field")
            .booleanType()
            .defaultValue(false)
            .withDescription("Optional flag to specify whether to fail if a field is missing or not, false by default");

    public static final ConfigOption<Boolean> IGNORE_PARSE_ERRORS = ConfigOptions
            .key("ignore-parse-errors")
            .booleanType()
            .defaultValue(false)
            .withDescription("Optional flag to skip fields and rows with parse errors instead of failing;\n"
                    + "fields are set to null in case of errors, false by default");

    public static final ConfigOption<String> DESC_PATH = ConfigOptions
            .key("descfilepath")
            .stringType()
            .noDefaultValue()
            .withDescription("protobuf desc file path");

    public static final ConfigOption<String> PROTO_CLASS = ConfigOptions
            .key("protoclass")
            .stringType()
            .noDefaultValue()
            .withDescription("protobuf class name to be decode or encode");

    @SuppressWarnings("unchecked")
    @Override
    public DecodingFormat<DeserializationSchema<RowData>> createDecodingFormat(
            DynamicTableFactory.Context context,
            ReadableConfig formatOptions) {
        FactoryUtil.validateFactoryOptions(this, formatOptions);
        validateFormatOptions(formatOptions);

        final boolean failOnMissingField = formatOptions.get(FAIL_ON_MISSING_FIELD);
        final boolean ignoreParseErrors = formatOptions.get(IGNORE_PARSE_ERRORS);
        final String descfilepath = formatOptions.get(DESC_PATH);
        final String protoclass = formatOptions.get(PROTO_CLASS);

        return new DecodingFormat<DeserializationSchema<RowData>>() {
            @Override
            public DeserializationSchema<RowData> createRuntimeDecoder(
                    DynamicTableSource.Context context,
                    DataType producedDataType) {
                final RowType rowType = (RowType) producedDataType.getLogicalType();
                final TypeInformation<RowData> rowDataTypeInfo =
                        (TypeInformation<RowData>) context.createTypeInformation(producedDataType);
                return new ProtobufDataDeserializationSchema(
                        rowType,
                        rowDataTypeInfo,
                        failOnMissingField,
                        ignoreParseErrors,
                        descfilepath,
                        protoclass
                );
            }

            @Override
            public ChangelogMode getChangelogMode() {
                return ChangelogMode.insertOnly();
            }
        };
    }

    @Override
    public EncodingFormat<SerializationSchema<RowData>> createEncodingFormat(
            DynamicTableFactory.Context context,
            ReadableConfig formatOptions) {
        FactoryUtil.validateFactoryOptions(this, formatOptions);

        TimestampFormat timestampOption = JsonOptions.getTimestampFormat(formatOptions);
        final String descfilepath = formatOptions.get(DESC_PATH);
        final String protoclass = formatOptions.get(PROTO_CLASS);

        return new EncodingFormat<SerializationSchema<RowData>>() {
            @Override
            public SerializationSchema<RowData> createRuntimeEncoder(
                    DynamicTableSink.Context context,
                    DataType consumedDataType) {
                final RowType rowType = (RowType) consumedDataType.getLogicalType();
                return new ProtobufDataSerializationSchema(rowType, descfilepath, protoclass);
            }

            @Override
            public ChangelogMode getChangelogMode() {
                return ChangelogMode.insertOnly();
            }
        };
    }

    @Override
    public String factoryIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public Set<ConfigOption<?>> requiredOptions() {
        Set<ConfigOption<?>> options = new HashSet<>();
        options.add(DESC_PATH);
        options.add(PROTO_CLASS);
        return options;
    }

    @Override
    public Set<ConfigOption<?>> optionalOptions() {
        Set<ConfigOption<?>> options = new HashSet<>();
        options.add(FAIL_ON_MISSING_FIELD);
        options.add(IGNORE_PARSE_ERRORS);
        return options;
    }

    // ------------------------------------------------------------------------
    //  Validation
    // ------------------------------------------------------------------------

    static void validateFormatOptions(ReadableConfig tableOptions) {
        boolean failOnMissingField = tableOptions.get(FAIL_ON_MISSING_FIELD);
        boolean ignoreParseErrors = tableOptions.get(IGNORE_PARSE_ERRORS);
        if (ignoreParseErrors && failOnMissingField) {
            throw new ValidationException(FAIL_ON_MISSING_FIELD.key()
                    + " and "
                    + IGNORE_PARSE_ERRORS.key()
                    + " shouldn't both be true.");
        }
    }
}
