package org.seekay.contract.common.enrich;

import lombok.Setter;
import org.seekay.contract.model.domain.Contract;
import org.seekay.contract.model.domain.ContractRequest;

import java.util.Date;
import java.util.regex.Pattern;

import static org.seekay.contract.model.expression.Expressions.*;

@Setter
public class EnricherService {

  private static Pattern contractExpressionPattern = Pattern.compile(CONTRACT_EXPRESSION);
  private static Pattern anyStringPattern = Pattern.compile(ANY_STRING);
  private static Pattern currentTimeStampPattern = Pattern.compile(TIMESTAMP);

  public void enrichRequest(Contract contract) {
    if (contract != null) {
      ContractRequest request = contract.getRequest();
      String enrichedPath = enrichString(request.getPath());
      request.setPath(enrichedPath);
    }
  }

  public void enrichResponse(Contract contract) {
    if (contract != null) {
      String enrichedResponseBody = enrichString(contract.getResponse().getBody());
      contract.getResponse().setBody(enrichedResponseBody);
    }
  }

  private String enrichString(String input) {
    if(input == null || input.isEmpty()) {
      return input;
    }
    String output = input;
    if (containsAnExpression(output)) {
      output = replaceAnyString(output);
      output = replaceTimestamp(input, output);
    }
    return output;
  }

  private String replaceTimestamp(String input, String output) {
    if(currentTimeStampPattern.matcher(input).find()) {
      String currentTime = String.valueOf(new Date().getTime());
      output = output.replaceAll(TIMESTAMP, currentTime);
    }
    return output;
  }

  private String replaceAnyString(String output) {
    if (anyStringPattern.matcher(output).find()) {
      output = output.replaceAll(ANY_STRING, Dictionary.randomWord());
    }
    return output;
  }

  private boolean containsAnExpression(String text) {
    return contractExpressionPattern.matcher(text).find();
  }

}
