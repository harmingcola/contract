package org.seekay.contract.common.matchers;

import lombok.Setter;
import org.seekay.contract.common.match.common.ExpressionMatcher;
import org.seekay.contract.model.domain.Contract;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.seekay.contract.model.tools.MapTools.isSubMap;

@Setter
public class HeaderMatcher {

  private ExpressionMatcher expressionMatcher;

  public Set<Contract> isMatch(Set<Contract> contracts, Map<String, String> actualHeaders) {
    Set<Contract> results = new HashSet<>();

    for (Contract contract : contracts) {
      Map<String, String> contractHeaders = contract.getRequest().getHeaders();
      if (isExactMatch(contractHeaders, actualHeaders)) {
        results.add(contract);
      }
    }

    if(results.size() == 0) {
      for (Contract contract : contracts) {
        Map<String, String> contractHeaders = contract.getRequest().getHeaders();
        if (isMatch(contractHeaders, actualHeaders)) {
          results.add(contract);
        }
      }
    }

    return results;
  }

  public boolean isExactMatch(Map<String, String> contractHeaders, Map<String, String> actualHeaders) {
    if(contractHeaders == null && actualHeaders !=null) {
      return false;
    }
    if(contractHeaders != null && actualHeaders ==null) {
      return false;
    }
    if(isSubMap(contractHeaders, actualHeaders) && isSubMap(actualHeaders, contractHeaders)) {
      return true;
    }else if(isExpressionSubmap(contractHeaders, actualHeaders) && isExpressionSubmap(actualHeaders, contractHeaders)) {
      return true;
    }
    return false;
  }

  public boolean isMatch(Map<String, String> contractHeaders, Map<String, String> actualHeaders) {
    if (isSubMap(contractHeaders, actualHeaders)) {
      return true;
    } else if(isExpressionSubmap(contractHeaders, actualHeaders)) {
      return true;
    }
    return false;
  }

  private boolean isExpressionSubmap(Map<String, String> contractHeaders, Map<String, String> actualHeaders) {
    if(contractHeaders == null) {
      return true;
    }
    for (Map.Entry<String, String> contractEntry : contractHeaders.entrySet()) {
      if(expressionMatcher.containsAnExpression(contractEntry.getValue())) {
        if (!expressionMatcher.isMatch(contractEntry.getValue(), actualHeaders.get(contractEntry.getKey()))) {
          return false;
        }
      }else {
        return false;
      }
    }
    return true;
  }

}
