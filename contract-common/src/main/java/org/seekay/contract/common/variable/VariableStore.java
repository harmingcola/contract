package org.seekay.contract.common.variable;

import lombok.Setter;
import org.seekay.contract.common.match.common.ExpressionMatcher;
import org.seekay.contract.model.domain.Contract;
import org.seekay.contract.model.domain.ContractRequest;
import org.seekay.contract.model.domain.ContractResponse;

import java.util.HashMap;
import java.util.Map;

@Setter
public class VariableStore extends HashMap<String, Object> {

  private ExpressionMatcher expressionMatcher;

  private StringVariableExtractor stringVariableExtractor;

  public void updateForResponse(Contract contract, ContractResponse actualResponse) {
    updateFromHeaders(contract.getResponse().getHeaders(), actualResponse.getHeaders());
    updateFromStrings(contract.getResponse().getBody(), actualResponse.getBody());
  }

  public void updateForRequest(Contract contract, ContractRequest actualRequest) {
    updateFromHeaders(contract.getRequest().getHeaders(), actualRequest.getHeaders());
    updateFromStrings(contract.getRequest().getBody(), actualRequest.getBody());
    updateFromStrings(contract.getRequest().getPath(), actualRequest.getPath());
  }

  private void updateFromHeaders(Map<String, String> contractHeaders, Map<String, String> actualHeaders) {
    if(contractHeaders != null && actualHeaders!=null) {
      for(Entry<String, String> entry : contractHeaders.entrySet()) {
        String actualHeader = actualHeaders.get(entry.getKey());
        updateFromStrings(entry.getValue(), actualHeader);
      }
    }
  }

  private void updateFromStrings(String contractText, String actualText) {
    if(contractText != null && actualText != null) {
      if (expressionMatcher.isMatch(contractText, actualText)) {
        this.putAll(stringVariableExtractor.extract(contractText, actualText));
      }
    }
  }


}
