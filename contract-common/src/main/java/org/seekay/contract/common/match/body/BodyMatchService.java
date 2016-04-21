package org.seekay.contract.common.match.body;

import lombok.Setter;
import org.seekay.contract.model.domain.Contract;

import java.util.HashSet;
import java.util.Set;

@Setter
public class BodyMatchService implements BodyMatcher {

    private Set<Contract> results = new HashSet<Contract>();

    private Set<BodyMatcher> bodyMatchers;

    public Set<Contract> findMatches(Set<Contract> contracts, String actualBody) {
        for(BodyMatcher matcher : bodyMatchers) {
            results.addAll(matcher.findMatches(contracts, actualBody));
        }
        return results;
    }

    public boolean isMatch(String contractBody, String actualBody) {
        for(BodyMatcher matcher : bodyMatchers) {
            boolean matchFound = matcher.isMatch(contractBody, actualBody);
            if(matchFound) {
                return true;
            }
        }
        return false;
    }
}
