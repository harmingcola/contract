package org.seekay.contract.common.variable;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.seekay.contract.common.match.body.JsonBodyMatcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Setter
public class JsonBodyVariableExtractor {

  private ObjectMapper objectMapper;
  private StringVariableExtractor stringVariableExtractor;
  private JsonBodyMatcher jsonBodyMatcher;

  public Map<String,Object> extract(String contractBody, String actualBody) {
    if(!valid(contractBody, actualBody)){
      return new HashMap<>();
    }

    try {
      Object contractObject = objectMapper.readValue(contractBody, Object.class);
      Object actualObject = objectMapper.readValue(actualBody, Object.class);
      return extractFromObject(contractObject, actualObject, new HashMap<>());
    } catch (IOException e) {
      return new HashMap<>();
    }
  }

  private boolean valid(String contractBody, String actualBody) {
    if(contractBody == null && actualBody == null) {
      return false;
    }
    else if(contractBody == null || actualBody == null) {
      return false;
    }
    return true;
  }

  private Map<String, Object> extractFromObject(Object contractObject, Object actualObject, HashMap<String, Object> variables) {
    if (contractObject instanceof Map) {
      return extractFromMap((Map<String, Object>) contractObject, (Map<String, Object>) actualObject, variables);
    }
    else if (contractObject instanceof List) {
      return extractFromList((List) contractObject, (List) actualObject, variables);
    }
    else if (contractObject.getClass() != actualObject.getClass()) {
      variables.putAll(stringVariableExtractor.extract(contractObject.toString(), actualObject.toString()));
      return variables;
    }
    else if (contractObject instanceof String) {
      variables.putAll(stringVariableExtractor.extract(contractObject.toString(), actualObject.toString()));
      return variables;
    }
    return variables;
  }

  private Map<String, Object> extractFromMap(Map<String, Object> contractMap, Map<String, Object> actualMap, HashMap<String, Object> variables) {
    for(Map.Entry<String, Object> entry : contractMap.entrySet()) {
      variables.putAll(extractFromObject(entry.getValue(), actualMap.get(entry.getKey()), variables));
    }
    return variables;
  }

  private Map<String, Object> extractFromList(List contractList, List actualList, HashMap<String, Object> variables) {
    List<Object> poolToMatchFrom = new ArrayList<>(actualList);
    Object objectToBeRemovedFromPool = null;
    for(Object contractObject : contractList) {
      for(Object actualObject : poolToMatchFrom) {
        if(jsonBodyMatcher.doObjectsMatch(contractObject, actualObject)) {
          extractFromObject(contractObject, actualObject, variables);
          objectToBeRemovedFromPool = actualObject;
          break;
        }
      }
      if(objectToBeRemovedFromPool != null) {
        poolToMatchFrom.remove(objectToBeRemovedFromPool);
        objectToBeRemovedFromPool = null;
      }
    }
    return variables;
  }

}
