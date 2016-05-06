package org.seekay.contract.common.match.body;

import lombok.Setter;
import org.seekay.contract.common.match.common.ExpressionMatcher;

@Setter
public class ExpressionBodyMatcher implements BodyMatcher {

  private ExpressionMatcher expressionMatcher;

  public boolean isMatch(String contractBody, String actualBody) {
    return expressionMatcher.isMatch(contractBody, actualBody);
  }
}
