package org.seekay.contract.client.client;

import lombok.extern.slf4j.Slf4j;
import org.seekay.contract.common.ApplicationContext;
import org.seekay.contract.common.assertion.AssertionService;
import org.seekay.contract.common.match.body.BodyMatchingService;
import org.seekay.contract.common.matchers.HeaderMatcher;
import org.seekay.contract.configuration.GitConfigurationSource;
import org.seekay.contract.configuration.LocalConfigurationSource;
import org.seekay.contract.model.builder.ContractOperator;
import org.seekay.contract.model.domain.Contract;
import org.seekay.contract.model.domain.ContractRequest;
import org.seekay.contract.model.domain.ContractResponse;
import org.seekay.contract.model.tools.ContractTools;
import org.seekay.contract.model.tools.Http;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@Slf4j
public class ContractClient implements ContractOperator<ContractClient> {

  private List<Contract> contracts;
  private String path;

  private HeaderMatcher headerMatcher = ApplicationContext.headerMatcher();
  private BodyMatchingService bodyMatchingService = ApplicationContext.bodyMatchingService();
  private AssertionService assertionService = ApplicationContext.assertionService();

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
      log.info("Checking contract " + contract);
      ContractRequest request = contract.getRequest();
      ContractResponse response = Http.method(request.getMethod())
          .toPath(path + request.getPath())
          .withHeaders(request.getHeaders())
          .withBody(request.getBody())
          .execute()
          .toResponse();
      assertResponseIsValid(contract.getResponse(), response);
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

  private void assertResponseIsValid(ContractResponse contractResponse, ContractResponse actualResponse) {
    assertStatusCodesMatch(contractResponse, actualResponse);
    assertBodiesMatch(contractResponse, actualResponse);
    assertHeadersContained(contractResponse, actualResponse);
  }

  private void assertHeadersContained(ContractResponse contractResponse, ContractResponse actualResponse) {
    boolean headersContained = headerMatcher.isMatch(contractResponse.getHeaders(), actualResponse.getHeaders());
    assertThat("Response headers are expected to contain all Contract Headers", headersContained, is(true));
  }

  private void assertBodiesMatch(ContractResponse contractResponse, ContractResponse actualResponse) {
    assertionService.assertOnWildCards(contractResponse, actualResponse);
    boolean bodiesMatch = bodyMatchingService.isMatch(contractResponse.getBody(), actualResponse.getBody());
    assertThat("Response and Contract bodies are expected to match", bodiesMatch, is(true));
  }

  private void assertStatusCodesMatch(ContractResponse contractResponse, ContractResponse actualResponse) {
    assertThat("Response and Contract status codes are expected to match", actualResponse.getStatus(), is(contractResponse.getStatus()));
  }

}
