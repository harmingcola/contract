package org.seekay.contract.server.match;

import org.seekay.contract.model.domain.Method;
import org.seekay.contract.model.domain.Contract;

import java.util.HashSet;
import java.util.Set;

public class MethodMatcher {

    public Set<Contract> match(Set<Contract> contracts, Method method) {
        Set<Contract> matchingContracts = new HashSet<Contract>();
        for (Contract contract : contracts) {
            if (contract.getRequest().getMethod().equals(method)) {
                matchingContracts.add(contract);
            }
        }
        return matchingContracts;
    }

}
