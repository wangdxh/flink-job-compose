package com.kedacom.flinksql.format.protobuf;


import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.Message;
import com.kedacom.flinksql.CommonUtils;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.table.api.TableException;
import org.apache.flink.table.data.*;
import org.apache.flink.table.types.logical.*;
import org.apache.flink.table.types.logical.utils.LogicalTypeChecks;
import org.apache.flink.table.types.logical.utils.LogicalTypeUtils;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;

import static com.google.protobuf.Descriptors.FieldDescriptor.Type.*;
import static org.apache.flink.util.Preconditions.checkNotNull;

public class ProtobufDataDeserializationSchema implements DeserializationSchema<RowData> {
    private static final long serialVersionUID = 1L;

    /** Flag indicating whether to fail if a field is missing. */
    private final boolean failOnMissingField;

    /** Flag indicating whether to ignore invalid fields/rows (default: throw an exception). */
    private final boolean ignoreParseErrors;

    /** TypeInformation of the produced {@link RowData}. **/
    private final TypeInformation<RowData> resultTypeInfo;

    private final ProtobufDataDeserializationSchema.DeserializationRuntimeConverter runtimeConverter;

    private transient Descriptors.Descriptor pbDescritpor;
    private String descfilepath;
    private String protoclass;

    public ProtobufDataDeserializationSchema(
            RowType rowType,
            TypeInformation<RowData> resultTypeInfo,
            boolean failOnMissingField,
            boolean ignoreParseErrors,
            String descfilepath,
            String protoclass) {
        if (ignoreParseErrors && failOnMissingField) {
            throw new IllegalArgumentException(
                    "protobuf format doesn't support failOnMissingField and ignoreParseErrors are both enabled.");
        }
        this.resultTypeInfo = checkNotNull(resultTypeInfo);
        this.failOnMissingField = failOnMissingField;
        this.ignoreParseErrors = ignoreParseErrors;
        this.runtimeConverter = createRowConverter(checkNotNull(rowType));
        this.descfilepath = descfilepath;
        this.protoclass = protoclass;
        CreatePbDescriptor();
    }
    private void CreatePbDescriptor(){
        List<Descriptors.Descriptor> listdesc = CommonUtils.readprotobufdescfile(descfilepath);
        System.out.println(listdesc.size());
        for(Descriptors.Descriptor descriptor : listdesc){
            if (descriptor.getFullName().equals(protoclass)){
                pbDescritpor = descriptor;
                break;
            }
            System.out.println(descriptor.getFullName());
        }
        if (null == pbDescritpor){
            throw new IllegalArgumentException("can not find descriptor for " + protoclass);
        }
    }
    @Override
    public void open(InitializationContext context) throws Exception {
        // if u dont understand this, u do not understand flink
        CreatePbDescriptor();
    }

    @Override
    public RowData deserialize(byte[] message) throws IOException {
        try {
            DynamicMessage.Builder pbBuilder = DynamicMessage.newBuilder(pbDescritpor);
            final Message pbMessageRoot = pbBuilder.mergeFrom(message).build();
            return (RowData) runtimeConverter.convert(null, pbMessageRoot);
        } catch (Throwable t) {
            if (ignoreParseErrors) {
                return null;
            }
            throw new IOException("Failed to deserialize protobuf.", t);
        }
    }

    @Override
    public boolean isEndOfStream(RowData nextElement) {
        return false;
    }

    @Override
    public TypeInformation<RowData> getProducedType() {
        return resultTypeInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProtobufDataDeserializationSchema that = (ProtobufDataDeserializationSchema) o;
        return failOnMissingField == that.failOnMissingField &&
                ignoreParseErrors == that.ignoreParseErrors &&
                resultTypeInfo.equals(that.resultTypeInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(failOnMissingField, ignoreParseErrors, resultTypeInfo);
    }

    // -------------------------------------------------------------------------------------
    // Runtime Converters
    // -------------------------------------------------------------------------------------

    /**
     * Runtime converter that converts {@link Message}s into objects of Flink Table & SQL
     * internal data structures.
     */
    @FunctionalInterface
    private interface DeserializationRuntimeConverter extends Serializable {
        Object convert(FieldDescriptor pbfield, Object pbobj);
    }

    /**
     * Creates a runtime converter which is null safe.
     */
    private ProtobufDataDeserializationSchema.DeserializationRuntimeConverter createConverter(LogicalType type) {
        return wrapIntoNullableConverter(createNotNullConverter(type));
    }

    /**
     * Creates a runtime converter which assuming input object is not null.
     */
    private ProtobufDataDeserializationSchema.DeserializationRuntimeConverter createNotNullConverter(LogicalType type) {
        switch (type.getTypeRoot()) {
            case NULL:
                return (pbfield, pbobj) -> null;
            case BOOLEAN:
                return this::convertToBoolean;
            case TINYINT:
            case SMALLINT:
            case INTEGER:
            case INTERVAL_YEAR_MONTH:
                return this::convertToInt;
            case BIGINT:
            case INTERVAL_DAY_TIME:
                return this::convertToLong;
            case FLOAT:
                return this::convertToFloat;
            case DOUBLE:
                return this::convertToDouble;
            case CHAR:
            case VARCHAR:
                return this::convertToString;
            case BINARY:
            case VARBINARY:
                return this::convertToBytes;
            case DECIMAL:
                return createDecimalConverter((DecimalType) type);
            case ARRAY:
                return createArrayConverter((ArrayType) type);
            case MAP:
            case MULTISET:
                return createMapConverter((MapType) type);
            case ROW:
                return createRowConverter((RowType) type);
            case DATE:
            case TIME_WITHOUT_TIME_ZONE:
            case TIMESTAMP_WITHOUT_TIME_ZONE:
            case RAW:
            default:
                throw new UnsupportedOperationException("Unsupported type: " + type);
        }
    }

    private boolean convertToBoolean(FieldDescriptor pbfield, Object pbobj) {
        if (pbfield.getType() == BOOL){
            return (boolean)pbobj;
        }else{
            throw new RuntimeException("the field is not BOOL type in protobuf");
        }
    }

    private int convertToInt(FieldDescriptor pbfield, Object pbobj) {
        switch (pbfield.getType()){
            case INT32:
            case SFIXED32:
            case SINT32:
            case UINT32:
            case FIXED32:{
                return (int)pbobj;
            }
            default:{
                throw new RuntimeException("the field is not int type in protobuf but is "+pbfield.getType());
            }
        }
    }

    private long convertToLong(FieldDescriptor pbfield, Object pbobj) {
        switch (pbfield.getType()){
            case INT64:
            case SFIXED64:
            case SINT64:
            case UINT64:
            case FIXED64:{
                return (long)pbobj;
            }
            default:{
                throw new RuntimeException("the field is not int type in protobuf");
            }
        }
    }

    private double convertToDouble(FieldDescriptor pbfield, Object pbobj) {
        switch (pbfield.getType()){
            case DOUBLE:
            {
                return (double)pbobj;
            }
            default:{
                throw new RuntimeException("the field is not double type in protobuf");
            }
        }
    }

    private float convertToFloat(FieldDescriptor pbfield, Object pbobj) {
        switch (pbfield.getType()){
            case FLOAT:
            {
                return (float)pbobj;
            }
            default:{
                throw new RuntimeException("the field is not float type in protobuf");
            }
        }
    }


    private StringData convertToString(FieldDescriptor pbfield, Object pbobj) {
        if (pbfield.getType() == STRING){
            return StringData.fromString((String)pbobj);
        }else if (pbfield.getType() == ENUM){
            return StringData.fromString(((Descriptors.EnumValueDescriptor)pbobj).getName());
        }else{
            throw new RuntimeException("the field is not   string in protobuf");
        }
    }

    private byte[] convertToBytes(FieldDescriptor pbfield, Object pbobj) {
        if (pbfield.getType() == BYTES){
            return ((ByteString) pbobj).toByteArray();
        }else{
            throw new RuntimeException("the field is not   byte[] in protobuf");
        }
    }

    private ProtobufDataDeserializationSchema.DeserializationRuntimeConverter createDecimalConverter(DecimalType decimalType) {
        final int precision = decimalType.getPrecision();
        final int scale = decimalType.getScale();
        return (pbfield, pbobj) -> {
            BigDecimal bigDecimal = new BigDecimal(pbobj.toString());
            return DecimalData.fromBigDecimal(bigDecimal, precision, scale);
        };
    }


    private ProtobufDataDeserializationSchema.DeserializationRuntimeConverter createArrayConverter(ArrayType arrayType) {
        ProtobufDataDeserializationSchema.DeserializationRuntimeConverter elementConverter = createConverter(arrayType.getElementType());
        final Class<?> elementClass = LogicalTypeUtils.toInternalConversionClass(arrayType.getElementType());
        return (pbfield, pbobj) -> {
            if (!(pbobj instanceof List)){
                throw new RuntimeException("protobuf object is not a array");
            }

            final Object[] array = (Object[]) Array.newInstance(elementClass, ((List) pbobj).size());
            int i = 0;
            for(Object item : (List<?>)pbobj){
                array[i++] = elementConverter.convert(pbfield, item);
            }
            return new GenericArrayData(array);
        };
    }

    private ProtobufDataDeserializationSchema.DeserializationRuntimeConverter createMapConverter(MapType mapType) {
        throw new UnsupportedOperationException(
                "protobuf format doesn't support MAP  " +
                        "The map type is: " + mapType.asSummaryString());
    }

    private ProtobufDataDeserializationSchema.DeserializationRuntimeConverter createRowConverter(RowType rowType) {
        final ProtobufDataDeserializationSchema.DeserializationRuntimeConverter[] fieldConverters = rowType.getFields().stream()
                .map(RowType.RowField::getType)
                .map(this::createConverter)
                .toArray(ProtobufDataDeserializationSchema.DeserializationRuntimeConverter[]::new);
        final String[] fieldNames = rowType.getFieldNames().toArray(new String[0]);

        return (pbfield, pbobj) -> {
            if (pbfield != null && (pbfield.getType() != MESSAGE && pbfield.getType() != GROUP)){
                throw new RuntimeException("not message or group in protobuf");
            }
            Message msg = (Message) pbobj;
            int arity = fieldNames.length;
            GenericRowData row = new GenericRowData(arity);
            for (int i = 0; i < arity; i++) {
                String fieldName = fieldNames[i];
                FieldDescriptor pbfielddescitem = null;
                Object pbobjitem = null;
                for ( final Map.Entry<FieldDescriptor, Object> entry : msg.getAllFields().entrySet()) {
                    if (CommonUtils.getFieldName(entry.getKey()).equals(fieldName)){
                       pbobjitem = entry.getValue();
                       pbfielddescitem = entry.getKey();
                       break;
                    }
                }

                if (pbfielddescitem == null && pbobjitem == null){
                    pbfielddescitem = CommonUtils.getFieldDescFromDescriptor(msg.getDescriptorForType(), fieldName);
                    pbobjitem = pbfielddescitem.getDefaultValue();
                }
                Object convertedField = convertField(fieldConverters[i], fieldName, pbfielddescitem, pbobjitem);
                row.setField(i, convertedField);
            }
            return row;
        };
    }

    private Object convertField(
            ProtobufDataDeserializationSchema.DeserializationRuntimeConverter fieldConverter,
            String fieldName,
            FieldDescriptor pbfielddesc, Object pbobj) {
        if (pbfielddesc == null) {
            if (failOnMissingField) {
                throw new ProtobufDataDeserializationSchema.JsonParseException(
                        "Could not find field with name '" + fieldName + "'.");
            } else {
                return null;
            }
        } else {
            return fieldConverter.convert(pbfielddesc, pbobj);
        }
    }

    private ProtobufDataDeserializationSchema.DeserializationRuntimeConverter wrapIntoNullableConverter(
            ProtobufDataDeserializationSchema.DeserializationRuntimeConverter converter) {
        return (pbfielddesc, pbobj) -> {
            if (pbobj == null ) {
                return null;
            }
            try {
                return converter.convert(pbfielddesc, pbobj);
            } catch (Throwable t) {
                if (!ignoreParseErrors) {
                    throw t;
                }
                return null;
            }
        };
    }

    /**
     * Exception which refers to parse errors in converters.
     * */
    private static final class JsonParseException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public JsonParseException(String message) {
            super(message);
        }

        public JsonParseException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
