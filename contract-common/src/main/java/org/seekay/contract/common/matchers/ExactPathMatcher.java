package org.seekay.contract.common.matchers;

import org.seekay.contract.model.domain.Contract;

import java.util.HashSet;
import java.util.Set;

public class ExactPathMatcher {

    public Set<Contract> match(Set<Contract> contracts, final String path) {
		Set<Contract> results = new HashSet<Contract>();
		for(Contract contract : contracts) {
			if(contract.getRequest().getPath().equals(path)) {
				results.add(contract);
			}
		}
        return results;
	}

}
