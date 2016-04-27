package org.seekay.contract.common.match;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.seekay.contract.common.match.body.BodyMatchingService;
import org.seekay.contract.common.match.path.PathMatchingService;
import org.seekay.contract.common.matchers.HeaderMatcher;
import org.seekay.contract.common.matchers.MethodMatcher;
import org.seekay.contract.common.service.ContractService;
import org.seekay.contract.model.domain.Contract;
import org.seekay.contract.model.domain.ContractRequest;
import org.seekay.contract.model.domain.Method;

import java.util.Set;

import static org.seekay.contract.model.util.PrintTools.prettyPrint;
import static org.seekay.contract.model.util.SetTools.intersectingElements;

@Slf4j
@Setter
public class MatchingService {
  
  private ContractService contractService;
  private MethodMatcher methodMatcher;
  private PathMatchingService pathMatchingService;
  private HeaderMatcher headerMatcher;
  private BodyMatchingService bodyMatchingService;
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

  public Contract matchPutRequest(ContractRequest contractRequest) {
    logRequestAgainstContracts(contractRequest, contractService.read());
    Set<Contract> matchedByMethod = matchByMethod(contractRequest.getMethod());
    Set<Contract> matchedByPath = matchByPath(contractRequest);
    Set<Contract> matchedByHeaders = matchByHeaders(contractRequest);
    Set<Contract> matchedByBody = matchByBody(contractRequest);
    Contract result = getResult(matchedByMethod, matchedByPath, matchedByHeaders, matchedByBody);
    if(result == null) {
      log.info("No matching contracts found, partial matches Method: {}, Path: {}, Headers: {}, Body: {} ",
              matchedByMethod, matchedByPath, matchedByHeaders, matchedByBody);
    }
    return result;
  }

  public Contract matchDeleteRequest(ContractRequest contractRequest) {
    logRequestAgainstContracts(contractRequest, contractService.read());
    Set<Contract> matchedByMethod = matchByMethod(contractRequest.getMethod());
    Set<Contract> matchedByPath = matchByPath(contractRequest);
    Set<Contract> matchedByHeaders = matchByHeaders(contractRequest);
    return getResult(matchedByMethod, matchedByPath, matchedByHeaders);
  }

  private void logRequestAgainstContracts(ContractRequest contractRequest, Set<Contract> contracts) {
    log.info("Request received {}", prettyPrint(contractRequest, objectMapper));
    log.info("Available contracts : \n{}", prettyPrint(contracts, objectMapper));
  }
  
  private Set<Contract> matchByMethod(Method method) {
    return methodMatcher.findMatches(contractService.read(), method);
  }
  
  private Set<Contract> matchByPath(ContractRequest contractRequest) {
    return pathMatchingService.findMatches(contractService.read(), contractRequest.getPath());
  }
  
  private Set<Contract> matchByHeaders(ContractRequest contractRequest) {
    return headerMatcher.isMatch(contractService.read(), contractRequest.getHeaders());
  }
  
  private Set<Contract> matchByBody(ContractRequest contractRequest) {
    return bodyMatchingService.findMatches(contractService.read(), contractRequest.getBody());
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
