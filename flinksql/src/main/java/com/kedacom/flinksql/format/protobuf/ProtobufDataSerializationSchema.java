package com.kedacom.flinksql.format.protobuf;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.Message;
import com.kedacom.flinksql.CommonUtils;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.table.data.*;
import org.apache.flink.table.types.logical.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

import static com.google.protobuf.Descriptors.FieldDescriptor.Type.ENUM;
import static com.google.protobuf.Descriptors.FieldDescriptor.Type.STRING;

public class ProtobufDataSerializationSchema  implements SerializationSchema<RowData> {
    private static final long serialVersionUID = 1L;

    /** RowType to generate the runtime converter. */
    private final RowType rowType;

    /** The converter that converts internal data formats to JsonNode. */
    private final ProtobufDataSerializationSchema.SerializationRuntimeConverter runtimeConverter;

    private transient Descriptors.Descriptor pbDescritpor;
    private String descfilepath;
    private String protoclass;
    private Message.Builder pbbuilder;


    public ProtobufDataSerializationSchema(RowType rowType,String descfilepath, String protoclass) {
        this.rowType = rowType;
        this.runtimeConverter = createConverter(rowType);
        this.descfilepath = descfilepath;
        this.protoclass = protoclass;
        this.CreatePbDescriptor();
    }

    @Override
    public void open(InitializationContext context) throws Exception {
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
    public byte[] serialize(RowData row) {
        if (pbbuilder == null) {
            pbbuilder = DynamicMessage.newBuilder(pbDescritpor);
        }else{
            pbbuilder.clear();
        }

        try {
            runtimeConverter.convert(pbbuilder, null, row);
            return pbbuilder.build().toByteArray();
        } catch (Throwable t) {
            throw new RuntimeException("Could not serialize row '" + row + "'. " +
                    "Make sure that the schema matches the input.", t);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProtobufDataSerializationSchema that = (ProtobufDataSerializationSchema) o;
        return rowType.equals(that.rowType) && descfilepath.equals(descfilepath) && protoclass.equals(protoclass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowType, descfilepath, protoclass);
    }

    // --------------------------------------------------------------------------------
    // Runtime Converters
    // --------------------------------------------------------------------------------

    /**
     * Runtime converter that converts objects of Flink Table & SQL internal data structures
     * to corresponding Protobuf Message.
     */
    private interface SerializationRuntimeConverter extends Serializable {
        // in row converter it returns Message
        // others will return object
        // builder, pdfielddesc, sqlvalue
        Object convert(Message.Builder builder, FieldDescriptor pdfielddesc, Object sqlvalue);
    }

    /**
     * Creates a runtime converter which is null safe.
     */
    private ProtobufDataSerializationSchema.SerializationRuntimeConverter createConverter(LogicalType type) {
        return wrapIntoNullableConverter(createNotNullConverter(type));
    }

    /**
     * Creates a runtime converter which assuming input object is not null.
     */
    private ProtobufDataSerializationSchema.SerializationRuntimeConverter createNotNullConverter(LogicalType type) {
        switch (type.getTypeRoot()) {
            case NULL:
                return (builder, pdfielddesc, sqlvalue) -> null;
            case BOOLEAN:
                return (builder, pdfielddesc, sqlvalue) -> (boolean) sqlvalue;
            case TINYINT:
                return (builder, pdfielddesc, sqlvalue) -> (int)(byte)sqlvalue;
            case SMALLINT:
                return (builder, pdfielddesc, sqlvalue) -> (int)(short)sqlvalue;
            case INTEGER:
            case INTERVAL_YEAR_MONTH:
                return (builder, pdfielddesc, sqlvalue) -> (int)sqlvalue;
            case BIGINT:
            case INTERVAL_DAY_TIME:
                return (builder, pdfielddesc, sqlvalue) -> (long)sqlvalue;
            case FLOAT:
                return (builder, pdfielddesc, sqlvalue) -> (float) sqlvalue;
            case DOUBLE:
                return (builder, pdfielddesc, sqlvalue) -> (double)sqlvalue;
            case CHAR:
            case VARCHAR:
                // value is BinaryString
                return (builder, pdfielddesc, sqlvalue) -> {
                    if (pdfielddesc.getType() == ENUM){
                        Descriptors.EnumDescriptor enumType = pdfielddesc.getEnumType();
                        Object value = enumType.findValueByName(sqlvalue.toString());
                        if (value == null) {
                            throw new RuntimeException("Enum type " + enumType.getFullName() + "has no value named "
                                    + sqlvalue.toString());
                        }
                        return value;
                    }else if (pdfielddesc.getType() == STRING){
                        return sqlvalue.toString();
                    }else{
                        throw new RuntimeException("not a string in protobuf but if " + pdfielddesc.getType());
                    }
                };
            case BINARY:
            case VARBINARY:
                return (builder, pdfielddesc, sqlvalue) -> (byte[])sqlvalue;

            case DECIMAL:
                return createDecimalConverter();
            case ARRAY:
                return createArrayConverter((ArrayType) type);
            case ROW:
                return createRowConverter((RowType) type);
            case DATE:
            case TIME_WITHOUT_TIME_ZONE:
            case TIMESTAMP_WITHOUT_TIME_ZONE:
                // will  support
            case MAP:
            case MULTISET:
            case RAW:
                // will not  support
            default:
                throw new UnsupportedOperationException("Not support to parse type: " + type);
        }
    }

    private ProtobufDataSerializationSchema.SerializationRuntimeConverter createDecimalConverter() {
        return (builder, pdfielddesc, sqlvalue) -> {
            BigDecimal bd = ((DecimalData) sqlvalue).toBigDecimal();
            return bd.doubleValue();
        };
    }


    private ProtobufDataSerializationSchema.SerializationRuntimeConverter createArrayConverter(ArrayType type) {
        final LogicalType elementType = type.getElementType();
        final ProtobufDataSerializationSchema.SerializationRuntimeConverter elementConverter = createConverter(elementType);
        final List<Object> retlist = new LinkedList<>();

        return (builder, pdfielddesc, sqlvalue) -> {
            if (pdfielddesc.isRepeated() == false){
                throw new RuntimeException("no a array in protobuf");
            }
            retlist.clear();

            ArrayData array = (ArrayData) sqlvalue;
            int numElements = array.size();
            for (int i = 0; i < numElements; i++) {
                Object element = ArrayData.get(array, i, elementType);
                retlist.add(elementConverter.convert(builder, pdfielddesc, element));
            }
            // return a list to
            return retlist;
        };
    }

    private ProtobufDataSerializationSchema.SerializationRuntimeConverter createRowConverter(RowType type) {
        final String[] fieldNames = type.getFieldNames().toArray(new String[0]);
        final LogicalType[] fieldTypes = type.getFields().stream()
                .map(RowType.RowField::getType)
                .toArray(LogicalType[]::new);
        final ProtobufDataSerializationSchema.SerializationRuntimeConverter[] fieldConverters = Arrays.stream(fieldTypes)
                .map(this::createConverter)
                .toArray(ProtobufDataSerializationSchema.SerializationRuntimeConverter[]::new);
        final int fieldCount = type.getFieldCount();

        return (builder, pbfielddesc, sqlvalue) -> {

            if (pbfielddesc != null && pbfielddesc.getJavaType() != FieldDescriptor.JavaType.MESSAGE){
                throw new RuntimeException("not a message");
            }

            Message.Builder realbuilder = pbfielddesc == null ? builder : builder.newBuilderForField(pbfielddesc);

            Descriptors.Descriptor desc = realbuilder.getDescriptorForType();

            RowData row = (RowData) sqlvalue;
            for (int i = 0; i < fieldCount; i++) {
                String fieldName = fieldNames[i];
                Object field = RowData.get(row, i, fieldTypes[i]);
                FieldDescriptor fielddesctemp = CommonUtils.getFieldDescFromDescriptor(desc, fieldName);
                if (fielddesctemp != null){
                    Object ret = fieldConverters[i].convert(realbuilder, fielddesctemp, field);
                    if (fielddesctemp.isRepeated() && ret instanceof List){
                        for (Object item : (List)ret){
                            realbuilder.addRepeatedField(fielddesctemp, item);
                        }
                    }else{
                        realbuilder.setField(fielddesctemp, ret);
                    }

                }else{
                    System.out.println("not find desc for " + fieldName);
                }
            }

            return realbuilder.build();
        };
    }

    private ProtobufDataSerializationSchema.SerializationRuntimeConverter wrapIntoNullableConverter(
            ProtobufDataSerializationSchema.SerializationRuntimeConverter converter) {
        return (builder, pdfielddesc, sqlvalue) -> {
            if (sqlvalue == null) {
                // if is null , will just set default value into message, not need to convert
                return pdfielddesc.getDefaultValue();
            }
            return converter.convert(builder, pdfielddesc, sqlvalue);
        };
    }
}

