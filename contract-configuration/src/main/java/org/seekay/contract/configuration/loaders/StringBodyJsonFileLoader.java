package org.seekay.contract.configuration.loaders;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.seekay.contract.model.domain.Contract;

import java.io.IOException;

public class StringBodyJsonFileLoader implements ContractFileLoader {

	private ObjectMapper objectMapper = new ObjectMapper();

	private String contractDefinition;

	public StringBodyJsonFileLoader(String contractDefinition) {
		this.contractDefinition = contractDefinition;
	}

	public Contract load() {
		try{
			Contract contract = objectMapper.readValue(contractDefinition, Contract.class);
			return contract;
		} catch (IOException e) {
			throw new IllegalStateException("Problem occurred unmarshalling JSON", e);
		}
	}


}
