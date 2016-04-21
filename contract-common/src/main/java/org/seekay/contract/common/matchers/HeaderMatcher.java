package org.seekay.contract.common.matchers;

import org.seekay.contract.model.domain.Contract;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.seekay.contract.model.util.HeaderTools.isSubMap;

public class HeaderMatcher {

    public Set<Contract> isMatch(Set<Contract> contracts, Map<String, String> actualHeaders) {
        Set<Contract> results = new HashSet<Contract>();

        for(Contract contract : contracts) {
            Map<String, String> contractHeaders = contract.getRequest().getHeaders();
            if(isMatch(contractHeaders, actualHeaders)) {
                results.add(contract);
            }
        }

        return results;
    }


    public boolean isMatch(Map<String, String> contractHeaders, Map<String, String> actualHeaders) {
        return isSubMap(contractHeaders, actualHeaders);
    }

}
