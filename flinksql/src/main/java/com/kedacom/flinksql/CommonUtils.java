package com.kedacom.flinksql;

import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;
import com.kedacom.flinksql.json.Sqljobconfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

public class CommonUtils {

    public static final String FLINK_CHECKPOINT_PATH = "FLINK_CHECKPOINT_PATH";

    public static String getFieldName(Descriptors.FieldDescriptor field) {
        String strkey;
        if (field.isExtension()) {
            // We special-case MessageSet elements for compatibility with proto1.
            if (field.getContainingType().getOptions().getMessageSetWireFormat()
                    && (field.getType() == Descriptors.FieldDescriptor.Type.MESSAGE) && (field.isOptional())
                    // object equality
                    && (field.getExtensionScope() == field.getMessageType())) {
                strkey = field.getMessageType().getFullName();
            } else {
                strkey = field.getFullName();
            }
        } else {
            if (field.getType() == Descriptors.FieldDescriptor.Type.GROUP) {
                // Groups must be serialized with their original capitalization.
                strkey = field.getMessageType().getName();
            } else {
                strkey = field.getName();
            }
        }
        return strkey;
    }

    public static List<Descriptors.Descriptor> readprotobufdescfile(String strpath) {
        try {
            String string = "";
            InputStream inputStream = new FileInputStream(strpath);
            byte[] buff;
            int filelen = inputStream.available();
            buff = new byte[filelen];
            int readlen = 0;
            int len;
            while (readlen < filelen && (len = inputStream.read(buff, 0, filelen - readlen)) != -1) {
                readlen += len;
            }

            return getDescriptorfromdescfile(buff);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Descriptors.FieldDescriptor getFieldDescFromDescriptor(Descriptors.Descriptor type, String name) {
        Descriptors.FieldDescriptor field;
        field = type.findFieldByName(name);

        // Group names are expected to be capitalized as they appear in the
        // .proto file, which actually matches their type names, not their field
        // names.
        if (field == null) {
            // Explicitly specify US locale so that this code does not break when
            // executing in Turkey.
            String lowerName = name.toLowerCase(Locale.US);
            field = type.findFieldByName(lowerName);
            // If the case-insensitive match worked but the field is NOT a group,
            if ((field != null) && (field.getType() != Descriptors.FieldDescriptor.Type.GROUP)) {
                field = null;
            }
        }
        // Again, special-case group names as described above.
        if ((field != null) && (field.getType() == Descriptors.FieldDescriptor.Type.GROUP)
                && !field.getMessageType().getName().equals(name)) {
            field = null;
        }
        return field;
    }

    public static List<Descriptors.Descriptor> getDescriptorfromdescfile(byte[] descdata) {
        List<Descriptors.Descriptor> listpbDescritpor = new ArrayList<>();

        //desc 是proto文件的集合，尤其有依赖时,多个proto文件的定义在一个desc文件里面。
        DescriptorProtos.FileDescriptorSet descriptorSet = null;
        try {
            descriptorSet = DescriptorProtos.FileDescriptorSet.parseFrom(descdata);
            //.parseFrom(new FileInputStream(strpath));
        } catch (IOException e) {
            e.printStackTrace();
            return listpbDescritpor;
        }

        ArrayList<Descriptors.FileDescriptor> filedesclist = new ArrayList<>();

        //解析每个proto文件的描述信息，有include时，需要把依赖的文件填进来
        for (DescriptorProtos.FileDescriptorProto fdp : descriptorSet.getFileList()) {
            //解析出来的结构体描述数组，用于结构体依赖
            Descriptors.FileDescriptor[] filedescarray = new Descriptors.FileDescriptor[filedesclist.size()];
            filedesclist.toArray(filedescarray);

            //从单个文件中解析文件描述
            Descriptors.FileDescriptor fileDescriptor = null;
            try {
                fileDescriptor = Descriptors.FileDescriptor.buildFrom(fdp, filedescarray);
            } catch (Descriptors.DescriptorValidationException e) {
                e.printStackTrace();
                return null;
            }
            filedesclist.add(fileDescriptor);

            //一个proto文件里面可能会定义多个结构体，这里遍历一下
            for (Descriptors.Descriptor descriptor : fileDescriptor.getMessageTypes()) {
                //得到每个结构体的详细信息
                String className = fdp.getOptions().getJavaPackage() + "."
                        + fdp.getOptions().getJavaOuterClassname() + "$"
                        + descriptor.getName();
                System.out.println(descriptor.getFullName() + " -> " + className);
                for (Descriptors.FieldDescriptor field : descriptor.getFields()) {
                    System.out.println("" + field.getName() + "-----" + field.getType().toString());
                }
                listpbDescritpor.add(descriptor);
            }
        }
        return listpbDescritpor;
    }

    public static String readfile(String strpath) {
        String string = "";
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(strpath);
        byte[] buff;

        try {

            buff = new byte[inputStream.available()];
            int len;
            while ((len = inputStream.read(buff)) != -1) {
                string += new String(buff, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return string;
    }


    public static byte[] readAllBytes(InputStream source, int initialSize) throws IOException {
        final int MAX_BUFFER_SIZE = Integer.MAX_VALUE - 8;
        final int BUFFER_SIZE = 8192;

        int capacity = initialSize;
        byte[] buf = new byte[capacity];
        int nread = 0;
        int n;
        for (; ; ) {
            // read to EOF which may read more or less than initialSize (eg: file
            // is truncated while we are reading)
            while ((n = source.read(buf, nread, capacity - nread)) > 0)
                nread += n;

            // if last call to source.read() returned -1, we are done
            // otherwise, try to read one more byte; if that failed we're done too
            if (n < 0 || (n = source.read()) < 0)
                break;

            // one more byte was read; need to allocate a larger buffer
            if (capacity <= MAX_BUFFER_SIZE - capacity) {
                capacity = Math.max(capacity << 1, BUFFER_SIZE);
            } else {
                if (capacity == MAX_BUFFER_SIZE)
                    throw new OutOfMemoryError("Required array size too large");
                capacity = MAX_BUFFER_SIZE;
            }
            buf = Arrays.copyOf(buf, capacity);
            buf[nread++] = (byte) n;
        }
        return (capacity == nread) ? buf : Arrays.copyOf(buf, nread);
    }

    public static Sqljobconfig getSqlJobFromTool(String sqlJobFilePath, String sqlJobContent) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        if (!StringUtils.isEmpty(sqlJobFilePath)) {
            return mapper.readValue(new File(sqlJobFilePath), Sqljobconfig.class);
        }

        if (!StringUtils.isEmpty(sqlJobContent)) {
            // Base64 Decode
            sqlJobContent = new String(Base64.getDecoder().decode(sqlJobContent.getBytes()), "utf-8");
            return mapper.readValue(sqlJobContent, Sqljobconfig.class);
        }

        throw new RuntimeException("sqlJsonFile or sqlJsonContent parameters not set");
    }

    public static void jsonSchemaValidator(String jsonSchemaContent, Object obj) throws Exception {
        if (StringUtils.isBlank(jsonSchemaContent)) {
            return;
        }
        try {
            JSONObject rawSchema = new JSONObject(jsonSchemaContent);
            Schema schema = SchemaLoader.load(rawSchema);

            schema.validate(new JSONObject(obj));
        } catch (ValidationException e) {
            ObjectMapper objectMapper = new ObjectMapper();
            Object errMsgObj = objectMapper.readValue(e.toJSON().toString(), Object.class);
            throw new Exception(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(errMsgObj));
        }
    }
}
