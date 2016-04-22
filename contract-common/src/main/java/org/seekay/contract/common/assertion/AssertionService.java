package org.seekay.contract.common.assertion;

import lombok.Setter;
import org.seekay.contract.model.domain.ContractResponse;

import java.util.Set;

@Setter
public class AssertionService implements Asserter {

    private Set<Asserter> asserters;

    public void assertOnWildCards(ContractResponse contractResponse, ContractResponse actualResponse) {
        for(Asserter asserter : asserters) {
            asserter.assertOnWildCards(contractResponse, actualResponse);
        }
    }
}
