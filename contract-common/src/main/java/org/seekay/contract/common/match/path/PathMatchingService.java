package org.seekay.contract.common.match.path;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.seekay.contract.model.domain.Contract;

import java.util.HashSet;
import java.util.Set;

/**
 * Matches the path a request is made on with its contract.
 * <p>
 * 1. Looks for an exact match
 * 2. Looks for a match with query params out of order
 * 3. Looks for a match with query params out of order with expressions
 * 4. Looks for a match with expressions
 */
@Slf4j
@Setter
public class PathMatchingService {

  private ExactPathMatcher exactPathMatcher;

  private QueryParamPathMatcher queryParamPathMatcher;

  private ExpressionQueryParamPathMatcher expressionQueryParamPathMatcher;

  private ExpressionPathMatcher expressionPathMatcher;

  public Set<Contract> findMatches(Set<Contract> contracts, final String actualPath) {
    Set<Contract> results = new HashSet<Contract>();

    // Find exact matches
    for (Contract contract : contracts) {
      if (isMatch(exactPathMatcher, contract.getRequest().getPath(), actualPath)) {
        results.add(contract);
      }
    }

    // Find matches using expressions
    for (Contract contract : contracts) {
      if (isMatch(expressionPathMatcher, contract.getRequest().getPath(), actualPath)) {
        results.add(contract);
      }
    }

    // Find matches with query params out of order
    for (Contract contract : contracts) {
      if (isMatch(queryParamPathMatcher, contract.getRequest().getPath(), actualPath)) {
        results.add(contract);
      }
    }

    // Find matches with query params out of order using expressions
    for (Contract contract : contracts) {
      if (isMatch(expressionQueryParamPathMatcher, contract.getRequest().getPath(), actualPath)) {
        results.add(contract);
      }
    }

    return results;
  }

  private boolean isMatch(PathMatcher pathMatcher, String contractPath, String actualPath) {
    if (contractPath == null) {
      return true;
    }
    if (actualPath == null) {
      return false;
    }
    return pathMatcher.isMatch(contractPath, actualPath);
  }
}
