package org.seekay.contract.configuration.loaders;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.seekay.contract.model.domain.Contract;

import java.io.File;
import java.io.IOException;

import static org.seekay.contract.common.ApplicationContext.objectMapper;

public class StringBodyJsonFileLoader implements ContractFileLoader {

	private ObjectMapper objectMapper = objectMapper();

	private File file;

	public StringBodyJsonFileLoader(File file) {
		this.file = file;
	}

	public Contract load() {
		try{
			Contract contract = objectMapper.readValue(file, Contract.class);
			contract.addInfo("fileName", file.getName());
			return contract;
		} catch (IOException e) {
			throw new IllegalStateException("Problem occurred unmarshalling JSON", e);
		}
	}


}
