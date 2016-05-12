package org.seekay.contract.configuration.loaders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.seekay.contract.model.domain.Contract;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonBodyFileLoader implements ContractFileLoader {

  private Map<String, Object> contents;

  private ObjectMapper objectMapper = new ObjectMapper();

  public JsonBodyFileLoader(HashMap contents) {
    this.contents = contents;
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public Contract load() {

    try {
      Map<String, Object> request = (Map<String, Object>) contents.get("request");
      Map<String, Object> response = (Map<String, Object>) contents.get("response");

      String requestBodyString = objectMapper.writeValueAsString(request.get("body"));
      String responseBodyString = objectMapper.writeValueAsString(response.get("body"));

      request.put("body", requestBodyString);
      response.put("body", responseBodyString);

      String payload = objectMapper.writeValueAsString(contents);
      Contract contract = objectMapper.readValue(payload, Contract.class);
      return contract;
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Problem occurred creating contract from json", e);
    } catch (IOException e) {
      throw new IllegalStateException("Problem occurred creating contract from json", e);
    }

  }
}
