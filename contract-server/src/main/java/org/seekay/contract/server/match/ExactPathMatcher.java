package org.seekay.contract.server.match;

import org.seekay.contract.model.domain.Contract;

import java.util.HashSet;
import java.util.Set;

public class ExactPathMatcher {

    public Set<Contract> match(Set<Contract> contracts, final String path) {
		Set<Contract> matchingContracts = new HashSet<Contract>();
		for(Contract contract : contracts) {
			if(contract.getRequest().getPath().equals(path)) {
				matchingContracts.add(contract);
			}
		}
        return matchingContracts;
	}

}
