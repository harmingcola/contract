package org.seekay.contract.configuration.loaders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.seekay.contract.model.domain.Contract;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonBodyFileLoader {

  private ObjectMapper objectMapper = new ObjectMapper();

  public JsonBodyFileLoader() {
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public Contract load(HashMap contents) {
    try {
      rewriteBodiesAsStrings(contents);
      String payload = objectMapper.writeValueAsString(contents);
      Contract contract = objectMapper.readValue(payload, Contract.class);
      return contract;
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Problem occurred creating contract from json", e);
    } catch (IOException e) {
      throw new IllegalStateException("Problem occurred creating contract from json", e);
    }
  }

  private void rewriteBodiesAsStrings(HashMap<String, Object> contents) throws JsonProcessingException {
    for(Map.Entry<String, Object> entry: contents.entrySet()) {
      if(entry.getValue() instanceof Map) {
        rewriteBodiesAsStrings((HashMap<String, Object>) entry.getValue());
      }
      if(entry.getKey() == "body") {
        entry.setValue(objectMapper.writeValueAsString(entry.getValue()));
      }
    }
  }


}
