package org.seekay.contract.common.match.body;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.seekay.contract.common.match.common.ExpressionMatcher;

import java.io.IOException;
import java.util.*;

@Setter
@Slf4j
public class JsonBodyMatcher implements BodyMatcher {

  private ObjectMapper objectMapper;

  private ExpressionMatcher expressionMatcher;

  public boolean isMatch(String contractBody, String actualBody) {
    try {
      Object contractObject = objectMapper.readValue(contractBody, Object.class);
      Object actualObject = objectMapper.readValue(actualBody, Object.class);
      return doObjectsMatch(contractObject, actualObject);
    } catch (IOException e) {
      return false;
    }
  }

  public boolean doObjectsMatch(Object contractObject, Object actualObject) {
    if (contractObject == null && actualObject == null) {
      return true;
    } else if (contractObject == null || actualObject == null) {
      return false;
    } else if (contractObject.getClass() != actualObject.getClass()) {
      return doStringsMatch(contractObject.toString(), actualObject.toString());
    } else if (contractObject instanceof Map) {
      return doMapsMatch((Map<String, Object>) contractObject, (Map<String, Object>) actualObject);
    } else if (contractObject instanceof Number) {
      return doNumbersMatch((Number) contractObject, (Number) actualObject);
    } else if (contractObject instanceof String) {
      return doStringsMatch((String) contractObject, (String) actualObject);
    } else if (contractObject instanceof Boolean) {
      return doBooleansMatch((Boolean) contractObject, (Boolean) actualObject);
    } else if (contractObject instanceof List) {
      return doListsMatch((List) contractObject, (List) actualObject);
    }
    return true;
  }

  private boolean doListsMatch(List<Object> contractList, List<Object> actualList) {
    List<Object> poolToMatchFrom = new ArrayList<Object>(actualList);
    Object actualListMatch = null;
    int contractObjectCount = contractList.size();
    int matchCount = 0;
    for(Object contractObject : contractList) {
      for(Object actualObject : poolToMatchFrom) {
        if(doObjectsMatch(contractObject, actualObject)) {
          actualListMatch = actualObject;
          matchCount++;
          break;
        }
      }
      if(actualListMatch != null) {
        poolToMatchFrom.remove(actualListMatch);
        actualListMatch = null;
      }
    }
    return contractObjectCount == matchCount;
  }

  private boolean doBooleansMatch(Boolean contactBoolean, Boolean actualBoolean) {
    return contactBoolean.equals(actualBoolean);
  }

  private boolean doStringsMatch(String contractString, String actualString) {
    if(contractString.equals(actualString)) {
      return true;
    }
    else if(expressionMatcher.containsAnExpression(contractString)) {
      return expressionMatcher.isMatch(contractString, actualString);
    }
    return false;
  }

  private boolean doNumbersMatch(Number contractNumber, Number actualNumber) {
    return contractNumber.equals(actualNumber);
  }

  private boolean doMapsMatch(Map<String, Object> contractMap, Map<String, Object> actualMap) {
    Set<String> keysChecked = new HashSet<String>();
    for (String key : contractMap.keySet()) {
      keysChecked.add(key);
      Object contractObject = contractMap.get(key);
      Object actualObject = actualMap.get(key);
      if (!doObjectsMatch(contractObject, actualObject)) {
        return false;
      }
    }
    return true;
  }
}
