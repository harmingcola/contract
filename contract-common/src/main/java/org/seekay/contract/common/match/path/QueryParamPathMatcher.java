package org.seekay.contract.common.match.path;

import org.seekay.contract.common.tools.MapTools;
import org.seekay.contract.common.tools.QueryParamTools;

import java.util.Map;

public class QueryParamPathMatcher implements PathMatcher {

  public boolean isMatch(String contractPath, String actualPath) {
    if(contractPath.contains("?") && actualPath.contains("?")) {
      Map<String, String> contractParameters = QueryParamTools.extractParameters(contractPath);
      Map<String, String> actualParameters = QueryParamTools.extractParameters(actualPath);
      return MapTools.isSubMap(contractParameters, actualParameters);
    }
    else {
      return false;
    }
  }

}
