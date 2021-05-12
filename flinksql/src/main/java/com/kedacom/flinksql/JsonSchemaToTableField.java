package com.kedacom.flinksql;


import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import scala.annotation.meta.field;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

class Field{
    String name;
    String type;
    List<Field> subfields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Field> getSubfields() {
        return subfields;
    }

    public void setSubfields(List<Field> subfields) {
        this.subfields = subfields;
    }

    public Field(String name, String type, List<Field> subfields) {
        this.name = name;
        this.type = type;
        this.subfields = subfields;
    }

    public Field() {
    }
}
public class JsonSchemaToTableField {
    static private Field generateField(String name, JSONObject jsonobj) throws Exception {
        Field field = new Field();
        field.setName(name);

        String type = jsonobj.getString("type");
        switch (type){
            case "array":{
                field.setType("ARRAY");
                field.setSubfields(new LinkedList<>());
                JSONObject subjsonobj = jsonobj.getJSONObject("items");
                field.getSubfields().add(generateField("", subjsonobj));
                break;
            }
            case "object":{
                field.setType("ROW");
                field.setSubfields(new LinkedList<>());
                JSONObject jsonproperties = jsonobj.getJSONObject("properties");
                Iterator<String> keys = jsonproperties.keys();
                while (keys.hasNext()) {
                    String subfiledName = keys.next();
                    JSONObject subjsonobj = jsonproperties.getJSONObject(subfiledName);
                    field.getSubfields().add(generateField(subfiledName, subjsonobj));
                }
                break;
            }
            case "number":{
                field.setType("DECIMAL");
                break;
            }
            case "integer":{
                field.setType("BIGINT");
                break;
            }
            case "boolean":{
                field.setType("BOOLEAN");
                break;
            }
            case "string":{
                field.setType("VARCHAR");
                break;
            }
            default:{
                throw new Exception("bad json type");
            }
        }

        return field;
    }

    static public String FieldToString(Field field) throws Exception{
        String strret = "";
        String strname = field.getName();//.length() > 0 ? "'"+field.getName() + "'": "";

        if (field.getType().equals("ARRAY") || field.getType().equals("ROW")){
            strret = strname + " " + field.getType() + "< ";
            List<String> substring = new LinkedList<>();
            for (Field subfield : field.getSubfields()){
                substring.add(FieldToString(subfield));
            }
            strret += String.join(", ", substring);
            strret += " >";
        }else{
            strret = strname + " " + field.getType();
        }
        return strret;
    }

    static public Field generateFieldFromFile(String filepath) throws Exception{
        byte[] byteschema = Files.readAllBytes(Paths.get(filepath));
        JSONObject jsonobj = new JSONObject(new String(byteschema));
        return generateField("root", jsonobj);
    }

    public static void main(String[] args) throws Exception {
        Field field = generateFieldFromFile("/Users/wang/Desktop/GitHub/flinketl/flinksql/src/main/resources/jsonfilessql/sqljobconfig.json");
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.printf(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(field));
        System.out.println("");
        System.out.println(FieldToString(field));
        System.out.println("----");
        for (Field subfield : field.getSubfields()){
            System.out.println(FieldToString(subfield));
        }
    }
}
