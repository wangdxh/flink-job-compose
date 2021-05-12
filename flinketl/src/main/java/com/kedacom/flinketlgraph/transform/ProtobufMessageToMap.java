package com.kedacom.flinketlgraph.transform;

import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Message;
import com.google.protobuf.UnknownFieldSet;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ProtobufMessageToMap {

    public static Map<String, Object> MessageToMap(Message msg) throws Exception{
        Map<String, Object> dst = new HashMap<>();
        for ( final Map.Entry<FieldDescriptor, Object> entry : msg.getAllFields().entrySet()) {
            FieldDescriptor field = entry.getKey();
            Object value = entry.getValue();
            printField(field, value, dst);
        }
        //printUnknownFields(msg.getUnknownFields(), dst);
        return dst;
    }

    private static  void printField(FieldDescriptor field, Object obj, Map<String, Object> dst) throws Exception{
        String strkey;
        if (field.isExtension()) {
            // We special-case MessageSet elements for compatibility with proto1.
            if (field.getContainingType().getOptions().getMessageSetWireFormat()
                    && (field.getType() == FieldDescriptor.Type.MESSAGE) && (field.isOptional())
                    // object equality
                    && (field.getExtensionScope() == field.getMessageType())) {
                strkey = field.getMessageType().getFullName();
            } else {
                strkey = field.getFullName();
            }
        } else {
            if (field.getType() == FieldDescriptor.Type.GROUP) {
                // Groups must be serialized with their original capitalization.
                strkey = field.getMessageType().getName();
            } else {
                strkey = field.getName();
            }
        }

        // Done with the name, on to the value
        if (field.isRepeated()) {
            // Repeated field. Print each element.
            List<Object> fieldlist = new LinkedList<>();

            for(Object item : (List<?>)obj){
                fieldlist.add(printFieldValue(field, item));
            }

            dst.put(strkey, fieldlist);
        } else {
            dst.put(strkey, printFieldValue(field, obj));
        }
    }

    static private Object printFieldValue(FieldDescriptor field, Object obj) throws Exception {
        switch (field.getType()) {
            case INT32:
            case INT64:
            case SINT32:
            case SINT64:
            case SFIXED32:
            case SFIXED64:
            case FLOAT:
            case DOUBLE:
            case BOOL:
            case UINT32:
            case FIXED32:
            case UINT64:
            case FIXED64:
            case STRING:{
                return obj;
            }
            case BYTES: {
                return ((ByteString) obj).toByteArray();
            }
            case ENUM: {
                return ((EnumValueDescriptor)obj).getName();
            }
            case MESSAGE:
            case GROUP: {
                return ProtobufMessageToMap.MessageToMap((Message) obj);
            }
            default: {
                // or when default when can just return null
                throw new Exception(field.getType().toString() + ": protobuf type not support now ");
            }
        }
    }
    static private void printUnknownFields(UnknownFieldSet unknownFields, Map<String, Object> dst){
    }
}
