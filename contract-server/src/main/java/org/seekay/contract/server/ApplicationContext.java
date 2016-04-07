package org.seekay.contract.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.seekay.contract.common.matchers.WhiteSpaceIgnoringBodyMatcher;
import org.seekay.contract.server.builder.ContractBuilder;
import org.seekay.contract.server.match.ExactPathMatcher;
import org.seekay.contract.server.match.MethodMatcher;
import org.seekay.contract.server.service.MatchingService;
import org.seekay.contract.server.service.ContractService;
import org.seekay.contract.server.match.HeaderMatcher;

public class ApplicationContext {

	private static ContractService contractService;
	private static ObjectMapper objectMapper;
	private static ContractBuilder contractBuilder;
	private static MatchingService matchingService;
	private static ExactPathMatcher exactPathMatcher;
	private static MethodMatcher methodMatcher;
	private static HeaderMatcher headerMatcher;
	private static WhiteSpaceIgnoringBodyMatcher whiteSpaceIgnoringBodyMatcher;

    public static void clear() {
        contractService = null;
        objectMapper = null;
        contractBuilder = null;
        matchingService = null;
        exactPathMatcher = null;
        methodMatcher = null;
        headerMatcher = null;
		    whiteSpaceIgnoringBodyMatcher = null;
    }

    public static ContractService contractService() {
        if(contractService == null) {
            contractService = new ContractService();
        }
        return contractService;
    }

    public static ObjectMapper objectMapper() {
        if(objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }

    public static ContractBuilder contractBuilder() {
        if(contractBuilder == null) {
            contractBuilder = new ContractBuilder(objectMapper());
        }
        return contractBuilder;
    }

    public static MatchingService matchingService() {
        if(matchingService == null) {
            matchingService = new MatchingService();
            matchingService.setContractService(contractService());
            matchingService.setMethodMatcher(methodMatcher());
            matchingService.setExactPathMatcher(exactPathMatcher());
            matchingService.setHeaderMatcher(headerMatcher());
            matchingService.setWhiteSpaceIgnoringBodyMatcher(whiteSpaceIgnoringBodyMatcher());
            matchingService.setObjectMapper(objectMapper());
        }
        return matchingService;
    }

    public static ExactPathMatcher exactPathMatcher() {
        if(exactPathMatcher == null) {
            exactPathMatcher = new ExactPathMatcher();
        }
        return exactPathMatcher;
    }

    public static MethodMatcher methodMatcher() {
        if(methodMatcher == null) {
            methodMatcher = new MethodMatcher();
        }
        return methodMatcher;
    }

    public static HeaderMatcher headerMatcher() {
        if(headerMatcher == null) {
            headerMatcher = new HeaderMatcher();
        }
        return headerMatcher;
    }

	public static WhiteSpaceIgnoringBodyMatcher whiteSpaceIgnoringBodyMatcher() {
		if(whiteSpaceIgnoringBodyMatcher == null) {
			whiteSpaceIgnoringBodyMatcher = new WhiteSpaceIgnoringBodyMatcher();
		}
		return whiteSpaceIgnoringBodyMatcher;
	}
}
