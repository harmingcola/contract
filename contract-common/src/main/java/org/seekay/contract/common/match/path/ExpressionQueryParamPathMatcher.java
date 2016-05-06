package org.seekay.contract.common.match.path;

import lombok.Setter;
import org.seekay.contract.common.match.common.ExpressionMatcher;
import org.seekay.contract.model.tools.QueryParamTools;

import java.util.Map;

@Setter
public class ExpressionQueryParamPathMatcher implements PathMatcher {

  private ExpressionMatcher expressionMatcher;

  public boolean isMatch(String contractPath, String actualPath) {
    Map<String, String> contractParameters = QueryParamTools.extractParameters(contractPath);
    Map<String, String> actualParameters = QueryParamTools.extractParameters(actualPath);
    if(contractParameters.size() == 0) {
      return false;
    }
    return areParametersEquivalent(contractParameters, actualParameters);
  }

  private boolean areParametersEquivalent(Map<String, String> contractParameters, Map<String, String> actualParameters) {
    if(contractParameters.size() > actualParameters.size()) {
      return false;
    }
    for(String key : contractParameters.keySet()) {
      String contractValue = contractParameters.get(key);
      String actualValue = actualParameters.get(key);
      if(!expressionMatcher.isMatch(contractValue, actualValue)) {
        return false;
      }
    }
    return true;
  }

}