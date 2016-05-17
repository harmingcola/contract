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

  public  Collection<Contract> expandParameters(Contract sourceContract) {
    List<Contract> result = new ArrayList<Contract>();
    for (Map<String, Object> parameterMap : sourceContract.getParameters()) {
      Contract destinationContract = new Contract();
      ContractRequest destinationRequest = expandForContractRequest(sourceContract.getRequest(), parameterMap);
      ContractResponse destinationResponse = expandForContractResponse(sourceContract.getResponse(), parameterMap);
      destinationContract.setRequest(destinationRequest);
      destinationContract.setResponse(destinationResponse);
      result.add(destinationContract);
    }
    return result;
  }

  private ContractResponse expandForContractResponse(ContractResponse sourceResponse, Map<String, Object> parameterMap) {
    ContractResponse targetResponse = CloneTools.cloneResponse(sourceResponse);
    for(Map.Entry<String, Object> entry: parameterMap.entrySet()) {
      targetResponse.setBody(expandString(targetResponse.getBody(), entry.getKey(), entry.getValue()));
      targetResponse.setStatus(expandString(targetResponse.getStatus(), entry.getKey(), entry.getValue()));
      targetResponse.setHeaders(expandHeaders(targetResponse.getHeaders(), entry.getKey(), entry.getValue()));
    }
    return targetResponse;
  }

  private ContractRequest expandForContractRequest(ContractRequest sourceRequest, Map<String, Object> parameterMap) {
    ContractRequest targetRequest = CloneTools.cloneRequest(sourceRequest);
    for(Map.Entry<String, Object> entry: parameterMap.entrySet()) {
      targetRequest.setBody(expandString(targetRequest.getBody(), entry.getKey(), entry.getValue()));
      targetRequest.setPath(expandString(targetRequest.getPath(), entry.getKey(), entry.getValue()));
      targetRequest.setHeaders(expandHeaders(targetRequest.getHeaders(), entry.getKey(), entry.getValue()));
    }
    return targetRequest;
  }

  private String expandString(String text, String key, Object value) {
    if(text == null) {
      return null;
    }
    return text.replaceAll("\\$\\{contract.parameter."+ key +"\\}", value.toString());
  }

  private Map<String, String> expandHeaders(Map<String, String> headers, String key, Object value) {
    if(headers == null) {
      return null;
    }
    Map<String, String> result = new HashMap<String, String>();
    for(Map.Entry<String, String> entry : headers.entrySet()) {
      result.put(entry.getKey(), expandString(entry.getValue(), key, value));
    }
    return result;
  }

}
