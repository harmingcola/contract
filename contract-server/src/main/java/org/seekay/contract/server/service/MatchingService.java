package org.seekay.contract.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.seekay.contract.common.matchers.WhiteSpaceIgnoringBodyMatcher;
import org.seekay.contract.model.domain.Method;
import org.seekay.contract.model.domain.Contract;
import org.seekay.contract.model.domain.ContractRequest;
import org.seekay.contract.server.match.ExactPathMatcher;
import org.seekay.contract.server.match.HeaderMatcher;
import org.seekay.contract.server.match.MethodMatcher;

import java.util.Set;

import static org.seekay.contract.model.util.PrintTools.prettyPrint;
import static org.seekay.contract.model.util.SetTools.intersectingElements;

@Slf4j
@Setter
public class MatchingService {
  
  private ContractService contractService;
  
  private MethodMatcher methodMatcher;
  
  private ExactPathMatcher exactPathMatcher;
  
  private HeaderMatcher headerMatcher;
  
  private WhiteSpaceIgnoringBodyMatcher whiteSpaceIgnoringBodyMatcher;
  
  private ObjectMapper objectMapper;
  
  public Contract matchGetRequest(ContractRequest contractRequest) {
    logRequestAgainstContracts(contractRequest, contractService.read());
    Set<Contract> matchedByMethod = matchByMethod(contractRequest.getMethod());
    Set<Contract> matchedByPath = matchByPath(contractRequest);
    Set<Contract> matchedByHeaders = matchByHeaders(contractRequest);
    return getResult(matchedByMethod, matchedByPath, matchedByHeaders);
  }
  
  public Contract matchPostRequest(ContractRequest contractRequest) {
    logRequestAgainstContracts(contractRequest, contractService.read());
    Set<Contract> matchedByMethod = matchByMethod(contractRequest.getMethod());
    Set<Contract> matchedByPath = matchByPath(contractRequest);
    Set<Contract> matchedByHeaders = matchByHeaders(contractRequest);
    Set<Contract> matchedByBody = matchByBody(contractRequest);
    return getResult(matchedByMethod, matchedByPath, matchedByHeaders, matchedByBody);
  }
  
  private void logRequestAgainstContracts(ContractRequest contractRequest, Set<Contract> contracts) {
    //log.info("Request received {}", prettyPrint(contractRequest, objectMapper));
    //log.info("Available contracts : \n{}", prettyPrint(contracts, objectMapper));
  }
  
  private Set<Contract> matchByMethod(Method method) {
    return methodMatcher.match(contractService.read(), method);
  }
  
  private Set<Contract> matchByPath(ContractRequest contractRequest) {
    return exactPathMatcher.match(contractService.read(), contractRequest.getPath());
  }
  
  private Set<Contract> matchByHeaders(ContractRequest contractRequest) {
    return headerMatcher.match(contractService.read(), contractRequest.getHeaders());
  }
  
  private Set<Contract> matchByBody(ContractRequest contractRequest) {
    return whiteSpaceIgnoringBodyMatcher.matchRequestBody(contractService.read(), contractRequest.getBody());
  }
  
  private Contract getResult(Set<Contract>... contracts) {
    Set<Contract> matchedContracts = intersectingElements(contracts);
    switch (matchedContracts.size()) {
      case 0:
        return null;
      case 1:
        return matchedContracts.iterator().next();
      default:
        throw new IllegalStateException("Multiple matching contracts found for request :\n"
            + prettyPrint(matchedContracts, objectMapper));
    }
  }
  
}
