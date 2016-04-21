package org.seekay.contract.common.match.body;

import org.seekay.contract.model.domain.Contract;

import java.util.Set;

public interface BodyMatcher {

    Set<Contract> findMatches(Set<Contract> contracts, String actualBody);

    boolean isMatch(String contractBody, String actualBody);
}
