package org.seekay.contract.common.match.body;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.seekay.contract.model.domain.Contract;
import org.seekay.contract.model.domain.ContractRequest;

import java.util.HashSet;
import java.util.Set;

/**
 * Matches the body a request / response with contracts
 *
 * 1. Looks for an exact match ignoring whitespace
 * 2. Looks for a match with expressions
 * 3. Looks for a match with json elements out of order with expressions
 */
@Slf4j
@Setter
public class BodyMatchingService {

  // If you add another matcher, make sure its added in both methods below

  private WhiteSpaceIgnoringBodyMatcher whiteSpaceIgnoringBodyMatcher;

  private ExpressionBodyMatcher expressionBodyMatcher;

  private JsonBodyMatcher jsonBodyMatcher;

  private SymmetricJsonBodyMatcher symmetricJsonBodyMatcher;

  public Set<Contract> findMatches(Set<Contract> contracts, ContractRequest actualRequest) {
    Set<Contract> results = new HashSet<Contract>();

    // Find text matches ignoring whitespace
    for(Contract contract : contracts) {
      if(isMatch(whiteSpaceIgnoringBodyMatcher, contract.getRequest().getBody(), actualRequest.getBody())) {
        results.add(contract);
      }
    }

    // Find text matches using expressions
    if(results.isEmpty()) {
      for (Contract contract : contracts) {
        if (isMatch(expressionBodyMatcher, contract.getRequest().getBody(), actualRequest.getBody())) {
          results.add(contract);
        }
      }
    }

    // Look for symmetric json specific matches
    if(results.isEmpty()) {
      for(Contract contract : contracts) {
        if(isMatch(symmetricJsonBodyMatcher, contract.getRequest().getBody(), actualRequest.getBody())) {
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
    if(contractBody == null || contractBody.isEmpty()) {
      return true;
    }
    if(actualBody == null || actualBody.isEmpty()) {
      return true;
    }

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
    if(contractBody == null) {
      return false;
    }
    if(actualBody != null && actualBody.trim().isEmpty()) {
      return false;
    }
    Boolean result = bodyMatcher.isMatch(contractBody, actualBody);
    return result;
  }
}
