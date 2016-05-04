package org.seekay.contract.common.match.path;

import java.util.regex.Pattern;

public class ExpressionPathMatcher implements PathMatcher {

  public static final String CONTRACT_EXPRESSION = ".*\\$\\{contract\\..*\\}.*";
  public static final String ANY_STRING = "\\$\\{contract\\.anyString\\}";

  private Pattern contractExpressionPattern = Pattern.compile(CONTRACT_EXPRESSION);
  private Pattern anyStringPattern = Pattern.compile(ANY_STRING);

  public boolean isMatch(String contractPath, String actualPath) {
    if(!contractExpressionPattern.matcher(contractPath).matches()) {
      return false;
    }

    if(anyStringPattern.matcher(contractPath).find()) {
      String oneTimeStringRegex = contractPath.replaceAll(ANY_STRING, ".*");
      return actualPath.matches(oneTimeStringRegex);
    }
    return false;
  }
}
