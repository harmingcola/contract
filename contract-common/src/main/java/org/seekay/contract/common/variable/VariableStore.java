package org.seekay.contract.common.variable;

import lombok.Setter;
import org.seekay.contract.common.match.common.ExpressionMatcher;
import org.seekay.contract.model.domain.Contract;
import org.seekay.contract.model.domain.ContractResponse;

import java.util.HashMap;

@Setter
public class VariableStore extends HashMap<String, Object> {

  private ExpressionMatcher expressionMatcher;

  private StringVariableExtractor stringVariableExtractor;

  public void updateForResponse(Contract contract, ContractResponse actualResponse) {
    String contractResponseBody = contract.getResponse().getBody();
    String actualResponseBody = actualResponse.getBody();

    if(contractResponseBody != null && actualResponseBody != null) {
      if (expressionMatcher.isMatch(contractResponseBody, actualResponseBody)) {
        this.putAll(stringVariableExtractor.extract(contractResponseBody, actualResponseBody));
      }
    }
  }

}
