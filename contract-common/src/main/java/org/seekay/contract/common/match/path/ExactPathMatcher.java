package org.seekay.contract.common.match.path;

public class ExactPathMatcher implements PathMatcher {

	public boolean isMatch(String contractPath, String actualPath) {
		return contractPath.equals(actualPath);
	}
}
