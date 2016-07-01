package org.seekay.contract.common.variable;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static java.util.regex.Pattern.compile;
import static org.seekay.contract.model.expression.Expressions.VARIABLE;

@Slf4j
public class StringVariableExtractor {

  private static Pattern variablePattern = compile(VARIABLE);

  public Map<String,Object> extract(String contractString, String actualString) {

    try {
      Map<String, Object> extractedVariables = new HashMap<>();
      Matcher matcher = variablePattern.matcher(contractString);

      String bodyRegex = variablePattern.matcher(contractString).replaceAll("(.*)?");
      Matcher actualStringMatcher = Pattern.compile(bodyRegex).matcher(actualString.trim());
      actualStringMatcher.find();

      int i = 1;
      while (matcher.find()) {
        String variableKey = matcher.group(2);
        String variableValue = actualStringMatcher.group(i);
        extractedVariables.put(variableKey, variableValue);
        i++;
      }

      return extractedVariables;
    } catch (PatternSyntaxException e) {
      return new HashMap<>();
    } catch (IndexOutOfBoundsException e) {
      return new HashMap<>();
    }
  }
}
