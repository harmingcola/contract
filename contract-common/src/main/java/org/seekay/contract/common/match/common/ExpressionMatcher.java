package org.seekay.contract.common.match.common;

import java.util.regex.Pattern;

public class ExpressionMatcher {

  public static final String CONTRACT_EXPRESSION = ".*\\$\\{contract\\..*\\}.*";
  public static final String ANY_STRING = "\\$\\{contract\\.anyString\\}";

  private static Pattern contractExpressionPattern = Pattern.compile(CONTRACT_EXPRESSION);
  private static Pattern anyStringPattern = Pattern.compile(ANY_STRING);

  public boolean isMatch(String contractString, String actualString) {
    if(containsAnExpression(contractString)) {
      if (anyStringPattern.matcher(contractString).find()) {
        String oneTimeStringRegex = contractString.replaceAll(ANY_STRING, ".*");
        return actualString.matches(oneTimeStringRegex);
      }
      return false;
    }
    return false;
  }

  private boolean containsAnExpression(String text) {
    return contractExpressionPattern.matcher(text).find();
  }
}
