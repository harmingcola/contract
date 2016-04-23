package org.seekay.contract.configuration.loaders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.seekay.contract.model.domain.Contract;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.seekay.contract.common.ApplicationContext.objectMapper;

public class JsonBodyFileLoader implements ContractFileLoader {

	private Map<String, Object> contents;

	private ObjectMapper objectMapper = objectMapper();

	public JsonBodyFileLoader(HashMap contents) {
		this.contents = contents;
	}

	public Contract load() {

		try {
			Map<String, Object> request = (Map<String, Object>) contents.get("request");
			Map<String, Object> response = (Map<String, Object>) contents.get("response");

			Map<String, Object> requestBody = (Map<String, Object>) request.get("body");
			Map<String, Object> responseBody = (Map<String, Object>) response.get("body");

			String requestBodyString = objectMapper.writeValueAsString(requestBody);
			String responseBodyString = objectMapper.writeValueAsString(responseBody);

			request.put("body", requestBodyString);
			response.put("body", responseBodyString);

			String payload = objectMapper.writeValueAsString(contents);
			return objectMapper.readValue(payload, Contract.class);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException("Problem occurred creating contract from json", e);
		} catch (IOException e) {
			throw new IllegalStateException("Problem occurred creating contract from json", e);
		}

	}
}
