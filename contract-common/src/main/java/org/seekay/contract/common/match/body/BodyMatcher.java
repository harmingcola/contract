package org.seekay.contract.common.match.body;

public interface BodyMatcher {

    boolean isMatch(String contractBody, String actualBody);
}
