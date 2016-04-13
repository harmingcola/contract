package org.seekay.contract.common.matchers;

import org.seekay.contract.model.domain.Contract;

import java.util.HashSet;
import java.util.Set;

public class WhiteSpaceIgnoringBodyMatcher {

	public Set<Contract> matchRequestBody(Set<Contract> contracts, String actualBody) {
		Set<Contract> result = new HashSet<Contract>();

		for(Contract contract : contracts) {
			String expectedBody = contract.getRequest().getBody();
			if(expectedBody == null && actualBody != null) {
				break;
			} else if(expectedBody == null && actualBody == null) {
				result.add(contract);
			} else {
				String strippedExpectedBody = expectedBody.replaceAll("\\s", "");
				String strippedActualBody = actualBody.replaceAll("\\s", "");
				if (strippedActualBody.equals(strippedExpectedBody)) {
					result.add(contract);
				}
			}
		}

		return result;
	}
}
