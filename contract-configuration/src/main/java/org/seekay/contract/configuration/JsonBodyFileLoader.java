package org.seekay.contract.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.seekay.contract.model.domain.Contract;
import org.seekay.contract.model.domain.ContractMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonBodyFileLoader {

  private ObjectMapper objectMapper = new ObjectMapper();

  public JsonBodyFileLoader() {
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public Contract load(ContractMap contents) {
    try {
      rewriteBodiesContents(contents);
      String payload = objectMapper.writeValueAsString(contents);
      Contract contract = objectMapper.readValue(payload, Contract.class);
      return contract;
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Problem occurred creating contract from json", e);
    } catch (IOException e) {
      throw new IllegalStateException("Problem occurred creating contract from json", e);
    }
  }

  private void rewriteBodiesContents(ContractMap contents) throws IOException {
    rewriteSetupBlock(contents);
    rewriteRequest(contents);
    rewriteResponse(contents);
  }

  private void rewriteResponse(ContractMap contents) throws IOException {
    rewriteBodiesAsStrings(contents.getResponse());
  }

  private void rewriteRequest(ContractMap contents) throws IOException {
    rewriteBodiesAsStrings(contents.getRequest());
  }

  private void rewriteSetupBlock(ContractMap contents) throws IOException {
    if (contents.getSetup() != null){
      for (Map<String, Object> setupContents : contents.getSetup()) {
        rewriteBodiesAsStrings(setupContents);
      }
    }
  }

  private void rewriteBodiesAsStrings(Map<String, Object> contents) throws IOException {
    for(Map.Entry<String, Object> entry: contents.entrySet()) {
      if(entry.getValue() instanceof Map) {
        rewriteBodiesAsStrings((HashMap<String, Object>) entry.getValue());
      }
      if(entry.getKey() == "body" && (entry.getValue() instanceof Map || entry.getValue() instanceof List)) {
        entry.setValue(objectMapper.writeValueAsString(entry.getValue()));
      }
    }
  }


}
