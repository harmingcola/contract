package org.seekay.contract.model.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PrintTools {

    private PrintTools() {
        throw new IllegalStateException("Utility classes should never be constructed");
    }

    public static String prettyPrint(Object object, ObjectMapper objectMapper) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}
