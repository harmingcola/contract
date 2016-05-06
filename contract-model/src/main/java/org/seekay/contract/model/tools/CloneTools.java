package org.seekay.contract.model.tools;

import org.seekay.contract.model.domain.Contract;
import org.seekay.contract.model.domain.ContractRequest;
import org.seekay.contract.model.domain.ContractResponse;

import java.util.HashMap;
import java.util.Map;

public class CloneTools {

  public static Contract cloneContract(Contract sourceContract) {
    Contract targetContract = new Contract();
    targetContract.setInfo(cloneMap(sourceContract.getInfo()));
    targetContract.setRequest(cloneRequest(sourceContract.getRequest()));
    targetContract.setResponse(cloneResponse(sourceContract.getResponse()));
    return targetContract;
  }

  public static ContractRequest cloneRequest(ContractRequest sourceRequest) {
    ContractRequest targetRequest = new ContractRequest();
    targetRequest.setPath(sourceRequest.getPath());
    targetRequest.setMethod(sourceRequest.getMethod());
    targetRequest.setBody(sourceRequest.getBody());
    targetRequest.setHeaders(cloneMap(sourceRequest.getHeaders()));
    return targetRequest;
  }

  public static ContractResponse cloneResponse(ContractResponse sourceResponse) {
    ContractResponse targetResponse = new ContractResponse();
    targetResponse.setStatus(sourceResponse.getStatus());
    targetResponse.setBody(sourceResponse.getBody());
    targetResponse.setHeaders(cloneMap(sourceResponse.getHeaders()));
    return targetResponse;
  }

  public static <T> Map<String, T> cloneMap(Map<String, T> sourceMap) {
    if(sourceMap != null) {
      Map<String, T> targetMap = new HashMap<String, T>();
      for (Map.Entry<String, T> entry : sourceMap.entrySet()) {
        targetMap.put(entry.getKey(), entry.getValue());
      }
      return targetMap;
    }
    return null;
  }
}
