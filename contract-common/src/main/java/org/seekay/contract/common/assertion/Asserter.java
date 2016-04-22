package org.seekay.contract.common.assertion;

import org.seekay.contract.model.domain.ContractResponse;

public interface Asserter {

    void assertOnWildCards(ContractResponse contractResponse, ContractResponse actualResponse);
}
