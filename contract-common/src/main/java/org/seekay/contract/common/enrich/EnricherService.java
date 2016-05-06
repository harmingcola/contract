package org.seekay.contract.common.enrich;

import lombok.Setter;
import org.seekay.contract.model.domain.Contract;
import org.seekay.contract.model.domain.ContractRequest;
import org.seekay.contract.model.domain.ContractResponse;

import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

import static org.seekay.contract.model.tools.CloneTools.*;
import static org.seekay.contract.model.expression.Expressions.*;

@Setter
public class EnricherService {

  private static Pattern contractExpressionPattern = Pattern.compile(CONTRACT_EXPRESSION);
  private static Pattern anyStringPattern = Pattern.compile(ANY_STRING);
  private static Pattern currentTimeStampPattern = Pattern.compile(TIMESTAMP);

  public Contract enrichRequest(Contract contract) {
    if (contract != null && contract.getRequest() != null) {
      Contract clonedContract = cloneContract(contract);
      ContractRequest request = clonedContract.getRequest();
      request.setPath(enrichString(request.getPath()));
      request.setHeaders(enrichHeaders(request.getHeaders()));
      return clonedContract;
    }
    return contract;
  }

  public Contract enrichResponse(Contract contract) {
    if (contract != null && contract.getResponse() != null) {
      Contract clonedContract = cloneContract(contract);
      ContractResponse response = clonedContract.getResponse();
      response.setBody(enrichString(response.getBody()));
      response.setHeaders(enrichHeaders(response.getHeaders()));
      return clonedContract;
    }
    return contract;
  }

  private Map<String, String> enrichHeaders(Map<String, String> headers) {
    if(headers != null) {
      for (Map.Entry<String, String> entry : headers.entrySet()) {
        String value = entry.getValue();
        if (containsAnExpression(value)) {
          headers.put(entry.getKey(), enrichString(value));
        }
      }
    }
    return headers;
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
