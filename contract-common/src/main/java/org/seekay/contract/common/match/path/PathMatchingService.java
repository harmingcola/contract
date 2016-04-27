package org.seekay.contract.common.match.path;

import lombok.Setter;
import org.seekay.contract.model.domain.Contract;

import java.util.HashSet;
import java.util.Set;

@Setter
public class PathMatchingService {

  private Set<PathMatcher> pathMatchers;

  public Set<Contract> findMatches(Set<Contract> contracts, final String path) {
    Set<Contract> results = new HashSet<Contract>();
    for (Contract contract : contracts) {
      for (PathMatcher pathMatcher : pathMatchers) {
        if(pathMatcher.isMatch(contract.getRequest().getPath(), path)) {
          results.add(contract);
        }
      }
    }
    return results;
  }
}
