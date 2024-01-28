package com.classpropertiesoverridejava.classpropertiesoverridejava.services;


import com.classpropertiesoverridejava.classpropertiesoverridejava.models.Fields;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
public class CustomClassOverrideService {

    public static void main (String[] args) throws JsonProcessingException, CloneNotSupportedException {
        addFields();
    }

    public static void addFields () throws JsonProcessingException, CloneNotSupportedException {

        String unStructuresRule = "{" +
                "\"field3\": \"700000\", " +
                "\"field4\": \"ADD\", " +
                "\"field5\": \"REMOVE\" " +
                "}";

        Fields unStructuresRuleObject = new ObjectMapper().readValue(unStructuresRule, Fields.class);

        Fields switchFields = new Fields();
        switchFields.setField1("value1");
        switchFields.setField2("value2");
        switchFields.setField5("value5");
        switchFields.setField6("value6");

        System.out.println(String.format("Before: %s", switchFields));
        Fields switchFieldsClone = overWriteFields (switchFields, unStructuresRuleObject);
        System.out.println(String.format("After Clone: %s", switchFieldsClone));

    }

    public static Fields overWriteFields (Fields switchFields, Fields unStructuresRuleObject) throws CloneNotSupportedException {

        Fields switchFieldsClone = (Fields) switchFields.clone();

        Class<?> clazz = unStructuresRuleObject.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(unStructuresRuleObject);
                if (value != null) {
                    if (value.equals("ADD")) {
                        field.set(switchFieldsClone, "");
                    } else if (value.equals("REMOVE")) {
                        field.set(switchFieldsClone, null);
                    } else {
                        field.set(switchFieldsClone, value);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return switchFieldsClone;
    }

}
