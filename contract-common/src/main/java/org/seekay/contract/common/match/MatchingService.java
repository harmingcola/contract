package org.seekay.contract.common.match;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.seekay.contract.common.match.body.BodyMatchingService;
import org.seekay.contract.common.match.path.PathMatchingService;
import org.seekay.contract.common.matchers.HeaderMatcher;
import org.seekay.contract.common.matchers.MethodMatcher;
import org.seekay.contract.common.service.ContractService;
import org.seekay.contract.common.variable.VariableStore;
import org.seekay.contract.model.domain.Contract;
import org.seekay.contract.model.domain.ContractRequest;
import org.seekay.contract.model.domain.Method;

import java.util.Set;

import static org.seekay.contract.model.tools.PrintTools.prettyPrint;
import static org.seekay.contract.model.tools.SetTools.intersectingElements;

@Slf4j
@Setter
public class MatchingService {
  
  private ContractService contractService;

  private MethodMatcher methodMatcher;
  private PathMatchingService pathMatchingService;
  private HeaderMatcher headerMatcher;
  private BodyMatchingService bodyMatchingService;
  private VariableStore variableStore;

  private ObjectMapper objectMapper;
  
  public Contract matchGetRequest(ContractRequest contractRequest) {
    logRequest(contractRequest);
    Set<Contract> matchedByMethod = matchByMethod(contractRequest.getMethod());
    Set<Contract> matchedByPath = matchByPath(contractRequest);
    Set<Contract> matchedByHeaders = matchByHeaders(contractRequest);
    Contract match = getResult(matchedByMethod, matchedByPath, matchedByHeaders);
    if(match != null) {
      variableStore.updateForRequest(match, contractRequest);
    }
    logResponse(match);
    return match;
  }
  
  public Contract matchPostRequest(ContractRequest contractRequest) {
    logRequest(contractRequest);
    Set<Contract> matchedByMethod = matchByMethod(contractRequest.getMethod());
    Set<Contract> matchedByPath = matchByPath(contractRequest);
    Set<Contract> matchedByHeaders = matchByHeaders(contractRequest);
    Set<Contract> matchedByBody = matchByBody(contractRequest);
    Contract match = getResult(matchedByMethod, matchedByPath, matchedByHeaders, matchedByBody);
    if(match != null) {
      variableStore.updateForRequest(match, contractRequest);
    }
    logResponse(match);
    return match;
  }

  public Contract matchPutRequest(ContractRequest contractRequest) {
    logRequest(contractRequest);
    Set<Contract> matchedByMethod = matchByMethod(contractRequest.getMethod());
    Set<Contract> matchedByPath = matchByPath(contractRequest);
    Set<Contract> matchedByHeaders = matchByHeaders(contractRequest);
    Set<Contract> matchedByBody = matchByBody(contractRequest);
    Contract match = getResult(matchedByMethod, matchedByPath, matchedByHeaders, matchedByBody);
    if(match != null) {
      variableStore.updateForRequest(match, contractRequest);
    }
    logResponse(match);
    return match;
  }

  public Contract matchDeleteRequest(ContractRequest contractRequest) {
    logRequest(contractRequest);
    Set<Contract> matchedByMethod = matchByMethod(contractRequest.getMethod());
    Set<Contract> matchedByPath = matchByPath(contractRequest);
    Set<Contract> matchedByHeaders = matchByHeaders(contractRequest);
    Contract match = getResult(matchedByMethod, matchedByPath, matchedByHeaders);
    if(match != null) {
      variableStore.updateForRequest(match, contractRequest);
    }
    logResponse(match);
    return match;
  }

  public Contract matchHeadRequest(ContractRequest contractRequest) {
    logRequest(contractRequest);
    Set<Contract> matchedByMethod = matchByMethod(contractRequest.getMethod());
    Set<Contract> matchedByPath = matchByPath(contractRequest);
    Set<Contract> matchedByHeaders = matchByHeaders(contractRequest);
    Contract match = getResult(matchedByMethod, matchedByPath, matchedByHeaders);
    if(match != null) {
      variableStore.updateForRequest(match, contractRequest);
    }
    logResponse(match);
    return match;
  }

  public Contract matchOptionsRequest(ContractRequest contractRequest) {
    logRequest(contractRequest);
    Set<Contract> matchedByMethod = matchByMethod(contractRequest.getMethod());
    Set<Contract> matchedByPath = matchByPath(contractRequest);
    Set<Contract> matchedByHeaders = matchByHeaders(contractRequest);
    Contract match = getResult(matchedByMethod, matchedByPath, matchedByHeaders);
    if(match != null) {
      variableStore.updateForRequest(match, contractRequest);
    }
    logResponse(match);
    return match;
  }

  private void logRequest(ContractRequest contractRequest) {
    log.info("Request received {}", prettyPrint(contractRequest, objectMapper));
  }

  private void logResponse(Contract contract) {
    log.info("Returning response {}", prettyPrint(contract, objectMapper));
  }
  
  private Set<Contract> matchByMethod(Method method) {
    Set<Contract> matches =  methodMatcher.findMatches(contractService.readEnabled(), method);
    log.debug("Matched by method : {} ",  prettyPrint(matches, objectMapper));
    return matches;
  }
  
  private Set<Contract> matchByPath(ContractRequest contractRequest) {
    Set<Contract> matches = pathMatchingService.findMatches(contractService.readEnabled(), contractRequest.getPath());
    log.debug("Matched by path : {} ",  prettyPrint(matches, objectMapper));
    return matches;
  }
  
  private Set<Contract> matchByHeaders(ContractRequest contractRequest) {
    Set<Contract> matches = headerMatcher.isMatch(contractService.readEnabled(), contractRequest.getHeaders());
    log.debug("Matched by headers : {} ",  prettyPrint(matches, objectMapper));
    return matches;
  }
  
  private Set<Contract> matchByBody(ContractRequest contractRequest) {
    Set<Contract> matches = bodyMatchingService.findMatches(contractService.readEnabled(), contractRequest);
    log.info("Matched by body : {} ",  prettyPrint(matches, objectMapper));
    return matches;
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
