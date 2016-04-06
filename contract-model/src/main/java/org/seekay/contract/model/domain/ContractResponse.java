package org.seekay.contract.model.domain;

import lombok.Data;

import java.util.Map;

@Data
public class ContractResponse {

    private Integer status;

    private String body;

    private Map<String, String> headers;
}
