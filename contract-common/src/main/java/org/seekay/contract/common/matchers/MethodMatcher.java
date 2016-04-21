package org.seekay.contract.common.matchers;

import org.seekay.contract.model.domain.Method;
import org.seekay.contract.model.domain.Contract;

import java.util.HashSet;
import java.util.Set;

public class MethodMatcher {

    public Set<Contract> findMatches(Set<Contract> contracts, Method method) {
        Set<Contract> results = new HashSet<Contract>();
        for (Contract contract : contracts) {
            if (contract.getRequest().getMethod().equals(method)) {
                results.add(contract);
            }
        }
        return results;
    }

}
