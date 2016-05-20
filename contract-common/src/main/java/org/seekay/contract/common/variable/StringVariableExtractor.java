package org.seekay.contract.common.variable;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static org.seekay.contract.model.expression.Expressions.VARIABLE;

public class StringVariableExtractor {

  private static Pattern variablePattern = compile(VARIABLE);

  public Map<String,Object> extract(String contractString, String actualString) {

    Map<String, Object> extractedVariables = new HashMap<String, Object>();
    Matcher matcher = variablePattern.matcher(contractString);

    String bodyRegex = variablePattern.matcher(contractString).replaceAll("(.*)");
    Matcher actualStringMatcher = Pattern.compile(bodyRegex).matcher(actualString);
    actualStringMatcher.find();

    int i=1;
    while(matcher.find()) {
      String variableKey = matcher.group(2);
      String variableValue = actualStringMatcher.group(i);
      extractedVariables.put(variableKey, variableValue);
      i++;
    }
    return extractedVariables;
  }
}
