package org.seekay.contract.server.match;

import org.seekay.contract.model.domain.Contract;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.seekay.contract.model.util.HeaderTools.isSubMap;

public class HeaderMatcher {

    public Set<Contract> match(Set<Contract> contracts, Map<String, String> requestHeaders) {
        Set<Contract> result = new HashSet<Contract>();

        for(Contract contract : contracts) {
            Map<String, String> contractHeaders = contract.getRequest().getHeaders();
            if(isSubMap(contractHeaders, requestHeaders)) {
                result.add(contract);
            }
        }

        return result;
    }

}
