package org.seekay.contract.common.match.path;

public class ExactPathMatcher implements PathMatcher {

	public boolean isMatch(String contractPath, String actualPath) {
		contractPath = removeTrailingSlash(contractPath);
		actualPath = removeTrailingSlash(actualPath);
		return contractPath.equals(actualPath);
	}

	private String removeTrailingSlash(String input) {
		return input.replaceAll("/+$", "");
	}
}
