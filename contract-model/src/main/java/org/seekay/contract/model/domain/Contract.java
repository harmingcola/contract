package org.seekay.contract.model.domain;

import lombok.Data;

import java.util.Map;

@Data
public class Contract {

	private Map<String, Object> info;

    private ContractRequest request;

    private ContractResponse response;
}
