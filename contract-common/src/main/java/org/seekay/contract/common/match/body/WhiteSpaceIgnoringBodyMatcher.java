package org.seekay.contract.common.match.body;


public class WhiteSpaceIgnoringBodyMatcher implements BodyMatcher {

	public boolean isMatch(String contractBody, String actualBody) {
		if(contractBody == null && actualBody == null) {
			return true;
		} else if(contractBody == null && actualBody.isEmpty()) {
			return true;
		} else if(contractBody == null && actualBody != null) {
			return false;
		} else {
			String strippedExpectedBody = contractBody.replaceAll("\\s", "");
			String strippedActualBody = actualBody.replaceAll("\\s", "");
			if (strippedActualBody.equals(strippedExpectedBody)) {
				return true;
			}
		}
		return false;
	}
}
