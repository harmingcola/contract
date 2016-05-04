package org.seekay.contract.common.match.path;

import java.util.HashMap;
import java.util.Map;

public class QueryParamPathMatcher implements PathMatcher {

  public boolean isMatch(String contractPath, String actualPath) {
    Map<String, String> contractParameters = extractParameters(contractPath);
    Map<String, String> actualParameters = extractParameters(actualPath);
    if(contractParameters.size() == 0) {
      return false;
    }
    return areParametersEquivalent(contractParameters, actualParameters);
  }

  private boolean areParametersEquivalent(Map<String, String> contractParameters, Map<String, String> actualParameters) {
    if(contractParameters.size() != actualParameters.size()) {
      return false;
    }
    for(String key : contractParameters.keySet()) {
      String contractValue = contractParameters.get(key);
      String actualValue = actualParameters.get(key);
      if(!contractValue.equals(actualValue)) {
        return false;
      }
    }
    return true;
  }

  private Map<String, String> extractParameters(String path) {
    Map<String, String> result = new HashMap<String, String>();
    String[] pathChunks = path.split("\\?");
    if(pathChunks.length != 2) {
      return result;
    }
    String[] parameterChunks = pathChunks[1].split("&");
    for(String parameterChunk : parameterChunks) {
      String[] parameters = parameterChunk.split("=");
      if(parameters.length == 2) {
        result.put(parameters[0], parameters[1]);
      }
    }
    return result;
  }

}
