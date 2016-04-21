package org.seekay.contract.common.match.body;

import org.seekay.contract.model.domain.Contract;

import java.util.HashSet;
import java.util.Set;

public class WhiteSpaceIgnoringBodyMatcher implements BodyMatcher {

	public Set<Contract> findMatches(Set<Contract> contracts, String actualBody) {
		Set<Contract> results = new HashSet<Contract>();
		for(Contract contract : contracts) {
			String contractBody = contract.getRequest().getBody();
			if(isMatch(contractBody, actualBody)) {
				results.add(contract);
			}
		}
		return results;
	}


	public boolean isMatch(String contractBody, String actualBody) {
		if(contractBody == null && actualBody != null) {
			return false;
		} else if(contractBody == null && actualBody == null) {
			return true;
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
