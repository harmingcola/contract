package org.seekay.contract.common.match.path;

import lombok.Setter;
import org.seekay.contract.model.domain.Contract;

import java.util.HashSet;
import java.util.Set;

@Setter
public class PathMatchingService implements PathMatcher {

  private Set<PathMatcher> pathMatchers;

  public Set<Contract> findMatches(Set<Contract> contracts, final String path) {
    Set<Contract> results = new HashSet<Contract>();
    for(Contract contract : contracts) {
      if(isMatch(contract.getRequest().getPath(), path)) {
        results.add(contract);
      }
    }
    return results;
  }

  public boolean isMatch(String contractPath, String actualPath) {
    for(PathMatcher pathMatcher : pathMatchers) {
      boolean matchFound = pathMatcher.isMatch(contractPath, actualPath);
      if(matchFound) {
        return true;
      }
    }
    return false;
  }
}
