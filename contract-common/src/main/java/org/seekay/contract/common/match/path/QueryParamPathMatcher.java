package org.seekay.contract.common.match.path;

import org.seekay.contract.model.tools.MapTools;
import org.seekay.contract.model.tools.QueryParamTools;

import java.util.Map;

public class QueryParamPathMatcher implements PathMatcher {

  public boolean isMatch(String contractPath, String actualPath) {
    String contractPathBeforeParams = contractPath.split("\\?")[0];
    String actualPathBeforeParams = actualPath.split("\\?")[0];

    if(contractPathBeforeParams.equals(actualPathBeforeParams)) {
      Map<String, String> contractParameters = QueryParamTools.extractParameters(contractPath);
      Map<String, String> actualParameters = QueryParamTools.extractParameters(actualPath);
      return MapTools.isSubMap(contractParameters, actualParameters);
    }
    return false;
  }

}
