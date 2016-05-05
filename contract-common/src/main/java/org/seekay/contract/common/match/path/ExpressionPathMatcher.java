package org.seekay.contract.common.match.path;

import lombok.Setter;
import org.seekay.contract.common.match.common.ExpressionMatcher;

@Setter
public class ExpressionPathMatcher implements PathMatcher {

  private ExpressionMatcher expressionMatcher;

  public boolean isMatch(String contractPath, String actualPath) {
    return expressionMatcher.isMatch(contractPath, actualPath);
  }
}
