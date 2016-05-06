package org.seekay.contract.client.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.seekay.contract.common.ApplicationContext;
import org.seekay.contract.common.Http;
import org.seekay.contract.common.builder.ContractFailedExceptionBuilder;
import org.seekay.contract.common.enrich.EnricherService;
import org.seekay.contract.common.match.body.BodyMatchingService;
import org.seekay.contract.common.matchers.HeaderMatcher;
import org.seekay.contract.model.tools.ContractTools;
import org.seekay.contract.configuration.GitConfigurationSource;
import org.seekay.contract.configuration.LocalConfigurationSource;
import org.seekay.contract.model.builder.ContractOperator;
import org.seekay.contract.model.domain.Contract;
import org.seekay.contract.model.domain.ContractRequest;
import org.seekay.contract.model.domain.ContractResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.seekay.contract.model.tools.PrintTools.prettyPrint;

@Slf4j
public class ContractClient implements ContractOperator<ContractClient> {

  private List<Contract> contracts;
  private String path;

  private HeaderMatcher headerMatcher = ApplicationContext.headerMatcher();
  private BodyMatchingService bodyMatchingService = ApplicationContext.bodyMatchingService();
  private ObjectMapper objectMapper = new ObjectMapper();
  private EnricherService enricherService = ApplicationContext.enricherService();

  private ContractClient() {
    contracts = new ArrayList<Contract>();
  }

  /**
   * Builds a new ContractClient
   * @return
   */
  public static ContractClient newClient() {
    return new ContractClient();
  }

  /**
   * Sets the address the ContractClient will aim tests at
   * @param path
   * @return
   */
  public ContractClient againstPath(String path) {
    this.path = path;
    return this;
  }

  /**
   * Creates a new instance populated with supplied contracts
   * @param contracts
   * @return
   */
  public static ContractClient fromContracts(List<Contract> contracts) {
    ContractClient contractClient = new ContractClient();
    contractClient.setContracts(contracts);
    return contractClient;
  }

  /**
   * Executes all loaded contracts against the specified path.
   */
  public void runTests() {
    for (Contract contract : contracts) {
      log.info("Executing test contract " + prettyPrint(contract, objectMapper));

      contract = enricherService.enrichRequest(contract);
      ContractRequest request = contract.getRequest();

      ContractResponse response = Http.method(request.getMethod())
          .toPath(path + request.getPath())
          .withHeaders(request.getHeaders())
          .withBody(request.getBody())
          .execute()
          .toResponse();
      assertResponseIsValid(contract, response);
    }
  }

  /**
   * Setter for contracts
   * @param contracts
   */
  public void setContracts(List<Contract> contracts) {
    this.contracts = contracts;
  }

  public ContractClient withGitConfig(String repositoryUrl) {
    GitConfigurationSource source = new GitConfigurationSource(repositoryUrl);
    contracts.addAll(source.load());
    return this;
  }

  public ContractClient withGitConfig(String repositoryUrl, String username, String password) {
    GitConfigurationSource source = new GitConfigurationSource(repositoryUrl, username, password);
    contracts.addAll(source.load());
    return this;
  }


  public ContractClient withLocalConfig(String localSource) {
    LocalConfigurationSource source = new LocalConfigurationSource(localSource);
    contracts.addAll(source.load());
    return this;
  }

  public void addContracts(Contract... contracts) {
    this.contracts.addAll(asList(contracts));
  }

  public void addContract(Contract contract) {
    this.contracts.add(contract);
  }

  public ContractClient withLocalConfig(String... configLocations) {
    for (String localConfigLocation : configLocations) {
      contracts.addAll(new LocalConfigurationSource(localConfigLocation).load());
    }
    return this;
  }

  public ContractClient retainTags(String... tagsToRetain) {
    this.contracts = ContractTools.retainTags(this.contracts, tagsToRetain);
    return this;
  }

  public ContractClient excludeTags(String... tagsToExclude) {
    this.contracts = ContractTools.excludeTags(this.contracts, tagsToExclude);
    return this;
  }

  public ContractClient tags(Set<String> tagsToRetain, Set<String> tagsToExclude) {
    this.contracts = ContractTools.tags(this.contracts, tagsToRetain, tagsToExclude);
    return this;
  }

  private void assertResponseIsValid(Contract contract, ContractResponse actualResponse) {
    assertStatusCodesMatch(contract, actualResponse);
    assertBodiesMatch(contract, actualResponse);
    assertHeadersContained(contract, actualResponse);
  }

  private void assertHeadersContained(Contract contract, ContractResponse actualResponse) {
    boolean headersContained = headerMatcher.isMatch(contract.getResponse().getHeaders(), actualResponse.getHeaders());
    if(!headersContained) {
      throw ContractFailedExceptionBuilder
          .expectedContract(contract)
          .actualResponse(actualResponse)
          .errorMessage("Response headers are expected to contain all Contract Headers")
          .build();
    }
  }

  private void assertBodiesMatch(Contract contract, ContractResponse actualResponse) {
    boolean bodiesMatch = bodyMatchingService.isMatch(contract.getResponse().getBody(), actualResponse.getBody());
    if(!bodiesMatch) {
      throw ContractFailedExceptionBuilder
          .expectedContract(contract)
          .actualResponse(actualResponse)
          .errorMessage("Bodies are expected to match")
          .build();
    }
  }

  private void assertStatusCodesMatch(Contract contract, ContractResponse actualResponse) {
    if(!actualResponse.getStatus().equals(contract.getResponse().getStatus())) {
      throw ContractFailedExceptionBuilder
          .expectedContract(contract)
          .actualResponse(actualResponse)
          .statusCodes(contract.getResponse().getStatus(), actualResponse.getStatus())
          .build();
    }

  }

}
