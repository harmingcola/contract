package org.seekay.contract.model.domain;

import lombok.Data;

import java.util.Map;

@Data
public class ContractRequest {

    private Method method;

    private String path;

    private String body;

    private Map<String, String> headers;

}
