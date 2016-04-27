package org.seekay.contract.common.match.body;

import lombok.Setter;
import org.seekay.contract.model.domain.Contract;

import java.util.HashSet;
import java.util.Set;

@Setter
public class BodyMatchingService {

    private Set<BodyMatcher> bodyMatchers;

    public Set<Contract> findMatches(Set<Contract> contracts, String actualBody) {
        Set<Contract> results = new HashSet<Contract>();
        for(Contract contract : contracts) {
            if(isMatch(contract.getResponse().getBody(), actualBody)) {
                results.add(contract);
            }
        }
        return results;
    }

    public boolean isMatch(String contractBody, String actualBody) {
        if(bothAreNull(contractBody, actualBody)) {
            return true;
        } else if(contractIsNullResponseIsEmpty(contractBody, actualBody)) {
            return true;
        }else if(eitherAreNull(contractBody, actualBody)) {
            return false;
        }

        for(BodyMatcher bodyMatcher : bodyMatchers) {
            boolean matchFound = bodyMatcher.isMatch(contractBody, actualBody);
            if(matchFound) {
                return true;
            }
        }
        return false;
    }

    private boolean contractIsNullResponseIsEmpty(String contractBody, String actualBody) {
        return contractBody == null && actualBody.trim().isEmpty();
    }

    private boolean eitherAreNull(String contractBody, String actualBody) {
        if(contractBody == null && actualBody != null) {
            return true;
        }
        if(contractBody != null && actualBody == null) {
            return true;
        }
        return false;
    }

    private boolean bothAreNull(String contractBody, String actualBody) {
        return contractBody == null && actualBody == null;
    }
}
