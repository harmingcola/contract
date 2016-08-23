package org.seekay.contract.configuration;

import org.seekay.contract.model.domain.Contract;
import org.seekay.contract.model.domain.ContractRequest;
import org.seekay.contract.model.domain.ContractResponse;
import org.seekay.contract.model.tools.CloneTools;

import java.util.*;

public class ParameterExpander {

    public static boolean containsParameters(Contract contract) {
        return contract.getParameters() != null && !contract.getParameters().isEmpty();
    }

    public Collection<Contract> expandParameters(Contract sourceContract) {
        List<Contract> expandedContracts = new ArrayList<>();
        for (Map<String, Object> parameterMap : sourceContract.getParameters()) {
            expandedContracts.add(expandForContract(sourceContract, parameterMap));
        }
        return cleanExpressionsWithoutParameter(expandedContracts);
    }

    private List<Contract> cleanExpressionsWithoutParameter(List<Contract> contracts) {
        List<Contract> cleanedContracts = new ArrayList<>();
        for(Contract contract : contracts) {
            cleanedContracts.add(cleanExpressionsFromContract(contract));
        }
        return cleanedContracts;
    }

    private Contract cleanExpressionsFromContract(Contract contract) {
        Contract cleanedContract = new Contract();
        ContractRequest cleanedRequest = cleanRequest(contract.getRequest());
        ContractResponse cleanedResponse = cleanResponse(contract.getResponse());
        cleanedContract.setRequest(cleanedRequest);
        cleanedContract.setResponse(cleanedResponse);
        return cleanedContract;
    }

    private ContractResponse cleanResponse(ContractResponse response) {
        ContractResponse cleanResponse = CloneTools.cloneResponse(response);
        cleanResponse.setBody(cleanString(response.getBody()));
        cleanResponse.setHeaders(cleanHeaders(response.getHeaders()));
        cleanResponse.setStatus(cleanString(response.getStatus()));
        return cleanResponse;
    }

    private ContractRequest cleanRequest(ContractRequest request) {
        ContractRequest cleanRequest = CloneTools.cloneRequest(request);
        cleanRequest.setBody(cleanString(request.getBody()));
        cleanRequest.setPath(cleanString(request.getPath()));
        cleanRequest.setHeaders(cleanHeaders(request.getHeaders()));
        return cleanRequest;
    }

    private Map<String, String> cleanHeaders(Map<String, String> headers) {
        if (headers == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            result.put(entry.getKey(), cleanString(entry.getValue()));
        }
        return result;
    }

    private String cleanString(String text) {
        if(text == null) {
            return null;
        }
        return text.replaceAll("\\$\\{contract.parameter.*\\}", "");
    }

    private Contract expandForContract(Contract sourceContract, Map<String, Object> parameterMap) {
        Contract destinationContract = new Contract();
        expandSetupBlock(sourceContract, parameterMap, destinationContract);
        ContractRequest destinationRequest = expandForContractRequest(sourceContract.getRequest(), parameterMap);
        ContractResponse destinationResponse = expandForContractResponse(sourceContract.getResponse(), parameterMap);
        destinationContract.setRequest(destinationRequest);
        destinationContract.setResponse(destinationResponse);
        return destinationContract;
    }

    private void expandSetupBlock(Contract sourceContract, Map<String, Object> parameterMap, Contract destinationContract) {
        if (sourceContract.getSetup() != null) {
            LinkedList<Contract> destinationSetupContracts = new LinkedList<Contract>();
            for (Contract sourceSetupContract : sourceContract.getSetup()) {
                destinationSetupContracts.add(expandForContract(sourceSetupContract, parameterMap));
            }
            destinationContract.setSetup(destinationSetupContracts);
        }
    }

    private ContractResponse expandForContractResponse(ContractResponse sourceResponse, Map<String, Object> parameterMap) {
        ContractResponse targetResponse = CloneTools.cloneResponse(sourceResponse);
        for (Map.Entry<String, Object> entry : parameterMap.entrySet()) {
            targetResponse.setBody(expandString(targetResponse.getBody(), entry.getKey(), entry.getValue()));
            targetResponse.setStatus(expandString(targetResponse.getStatus(), entry.getKey(), entry.getValue()));
            targetResponse.setHeaders(expandHeaders(targetResponse.getHeaders(), entry.getKey(), entry.getValue()));
        }

        return targetResponse;
    }

    private ContractRequest expandForContractRequest(ContractRequest sourceRequest, Map<String, Object> parameterMap) {
        ContractRequest targetRequest = CloneTools.cloneRequest(sourceRequest);
        for (Map.Entry<String, Object> entry : parameterMap.entrySet()) {
            targetRequest.setBody(expandString(targetRequest.getBody(), entry.getKey(), entry.getValue()));
            targetRequest.setPath(expandString(targetRequest.getPath(), entry.getKey(), entry.getValue()));
            targetRequest.setHeaders(expandHeaders(targetRequest.getHeaders(), entry.getKey(), entry.getValue()));
        }
        return targetRequest;
    }

    private String expandString(String text, String key, Object value) {
        if (text == null) {
            return null;
        }
        return text.replaceAll("\\$\\{contract.parameter." + key + "\\}", value.toString());
    }

    private Map<String, String> expandHeaders(Map<String, String> headers, String key, Object value) {
        if (headers == null) {
            return null;
        }
        Map<String, String> result = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            result.put(entry.getKey(), expandString(entry.getValue(), key, value));
        }
        return result;
    }

}
