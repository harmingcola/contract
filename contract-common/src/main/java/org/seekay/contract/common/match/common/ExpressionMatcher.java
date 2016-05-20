package org.seekay.contract.common.match.common;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static java.util.regex.Pattern.*;
import static org.seekay.contract.model.expression.Expressions.*;

@Slf4j
public class ExpressionMatcher {

  private static Pattern contractExpressionPattern = compile(CONTRACT_EXPRESSION);
  private static Pattern anyStringPattern = compile(ANY_STRING);
  private static Pattern anyNumberPattern = compile(ANY_NUMBER);
  private static Pattern timeStampPattern = compile(TIMESTAMP);
  private static Pattern variablePattern = compile(VARIABLE);

  public boolean isMatch(String contractString, String actualString) {

    if(containsAnExpression(contractString)) {
      String oneTimeStringRegex = contractString;
      if (anyStringPattern.matcher(oneTimeStringRegex).find()) {
        oneTimeStringRegex = anyStringPattern.matcher(oneTimeStringRegex).replaceAll(".*?");
      }
      if (anyNumberPattern.matcher(oneTimeStringRegex).find()) {
        oneTimeStringRegex = anyNumberPattern.matcher(oneTimeStringRegex).replaceAll("-?[0-9]+(\\\\.[0-9]+)?");
      }
      if (timeStampPattern.matcher(oneTimeStringRegex).find()) {
        oneTimeStringRegex = timeStampPattern.matcher(oneTimeStringRegex).replaceAll(buildTimestampPattern());
      }
      if (variablePattern.matcher(oneTimeStringRegex).find()) {
        oneTimeStringRegex = variablePattern.matcher(oneTimeStringRegex).replaceAll(".*?");
      }

      try {
        if(actualString.matches(oneTimeStringRegex)) {
          return true;
        }
      } catch (PatternSyntaxException e) {
        log.info("Problem occurred compiling regex for : " + oneTimeStringRegex);
      }
    }
    return false;
  }

  private String buildTimestampPattern() {
    String timestamp = String.valueOf(new Date().getTime());
    timestamp = timestamp.substring(0,8);

    StringBuilder builder = new StringBuilder();
    builder.append(timestamp);
    builder.append("[0-9]{5}");

    return builder.toString();
  }

  public boolean containsAnExpression(String text) {
    return contractExpressionPattern.matcher(text).find();
  }
}
