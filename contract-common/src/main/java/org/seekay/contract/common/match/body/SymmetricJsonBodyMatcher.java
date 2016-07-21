package org.seekay.contract.common.match.body;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Setter
@Slf4j
public class SymmetricJsonBodyMatcher extends JsonBodyMatcher {

  @Override
  public boolean isMatch(String contractBody, String actualBody) {
    try {
      Object contractObject = objectMapper.readValue(contractBody, Object.class);
      Object actualObject = objectMapper.readValue(actualBody, Object.class);
      return (doObjectsMatch(contractObject, actualObject) && doObjectsMatch(actualObject, contractObject));
    } catch (IOException e) {
      return false;
    }
  }


}
