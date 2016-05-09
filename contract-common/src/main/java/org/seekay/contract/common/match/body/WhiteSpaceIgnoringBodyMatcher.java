package org.seekay.contract.common.match.body;


public class WhiteSpaceIgnoringBodyMatcher implements BodyMatcher {

	public boolean isMatch(String contractBody, String actualBody) {
		String strippedExpectedBody = contractBody.replaceAll("\\s", "");
		String strippedActualBody = actualBody.replaceAll("\\s", "");
		return strippedActualBody.equals(strippedExpectedBody);
	}
}
