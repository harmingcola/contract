package org.seekay.contract.common.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.seekay.contract.model.domain.Contract;

import java.io.IOException;

@Setter
public class ContractBuilder {

    private ObjectMapper objectMapper;

    public Contract fromJson(String contractDefinition) {
        Contract result;
        try {
            result = objectMapper.readValue(contractDefinition, Contract.class);
        } catch (IOException e) {
            throw new IllegalStateException("Problem occurred during converting json String to Contract", e);
        }
        return result;
    }
}
