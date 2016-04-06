package org.seekay.contract.model.domain;

import lombok.Data;

@Data
public class Contract {

    private ContractRequest request;

    private ContractResponse response;
}
