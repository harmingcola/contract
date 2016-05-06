package org.seekay.contract.common.match.body;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.seekay.contract.model.domain.Contract;
import org.seekay.contract.model.domain.ContractRequest;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Setter
public class BodyMatchingService {

  // If you add another matcher, make sure its added in both methods below

  private WhiteSpaceIgnoringBodyMatcher whiteSpaceIgnoringBodyMatcher;

  private ExpressionBodyMatcher expressionBodyMatcher;

  private JsonBodyMatcher jsonBodyMatcher;

  public Set<Contract> findMatches(Set<Contract> contracts, ContractRequest actualRequest) {
    Set<Contract> results = new HashSet<Contract>();

    // Find text matches ignoring whitespace
    for(Contract contract : contracts) {
      if(isMatch(whiteSpaceIgnoringBodyMatcher, contract.getRequest().getBody(), actualRequest.getBody())) {
        results.add(contract);
      }
    }

    // Find text matches ignoring whitespace
    if(results.isEmpty()) {
      for (Contract contract : contracts) {
        if (isMatch(expressionBodyMatcher, contract.getRequest().getBody(), actualRequest.getBody())) {
          results.add(contract);
        }
      }
    }

    // Look for json specific matches
    if(results.isEmpty()) {
      for(Contract contract : contracts) {
        if(isMatch(jsonBodyMatcher, contract.getRequest().getBody(), actualRequest.getBody())) {
          results.add(contract);
        }
      }
    }

    return results;
  }

  public boolean isMatch(String contractBody, String actualBody) {
    if(isMatch(whiteSpaceIgnoringBodyMatcher, contractBody, actualBody)) {
      return true;
    }
    else if (isMatch(expressionBodyMatcher, contractBody, actualBody)) {
      return true;
    }
    else if (isMatch(jsonBodyMatcher, contractBody, actualBody)) {
      return true;
    }
    return false;
  }

  private boolean isMatch(BodyMatcher bodyMatcher, String contractBody, String actualBody) {
    log.info("Checking {} against {} using {}", contractBody, actualBody, bodyMatcher);
    if(contractBody == null && actualBody == null) {
      return true;
    }
    if(contractIsNullResponseIsEmpty(contractBody, actualBody)) {
      return true;
    }
    if(contractBody == null ^ actualBody == null) {
      return false;
    }
    return bodyMatcher.isMatch(contractBody, actualBody);
  }

  private boolean contractIsNullResponseIsEmpty(String contractBody, String actualBody) {
    return contractBody == null && actualBody.trim().isEmpty();
  }


}
