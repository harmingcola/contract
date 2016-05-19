package org.seekay.contract.configuration.loaders;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.seekay.contract.model.domain.Contract;

import java.io.IOException;

public class StringBodyJsonFileLoader {

	private ObjectMapper objectMapper = new ObjectMapper();

	private String contractDefinition;

	public Contract load(String contractDefinition) {
		try{
			Contract contract = objectMapper.readValue(contractDefinition, Contract.class);
			return contract;
		} catch (IOException e) {
			throw new IllegalStateException("Problem occurred unmarshalling JSON", e);
		}
	}


}
