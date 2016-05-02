package org.seekay.contract.client.client;

import lombok.extern.slf4j.Slf4j;
import org.seekay.contract.common.ApplicationContext;
import org.seekay.contract.common.assertion.AssertionService;
import org.seekay.contract.common.match.body.BodyMatchingService;
import org.seekay.contract.common.matchers.HeaderMatcher;
import org.seekay.contract.configuration.GitConfigurationSource;
import org.seekay.contract.configuration.LocalConfigurationSource;
import org.seekay.contract.model.domain.Contract;
import org.seekay.contract.model.domain.ContractRequest;
import org.seekay.contract.model.domain.ContractResponse;
import org.seekay.contract.model.tools.Http;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@Slf4j
public class ContractClient {

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
	 * URL of the git repo from which contracts will be loaded
	 * @param repositoryUrl
	 * @return
	 */
	public ContractClient withGitConfig(String repositoryUrl) {
		GitConfigurationSource source = new GitConfigurationSource(repositoryUrl);
		contracts.addAll(source.load());
		return this;
	}

	/**
	 * URL of the secured git repo from which the contracts will be loaded
	 * @param repositoryUrl
	 * @param username
	 * @param password
	 * @return
	 */
	public ContractClient withGitConfig(String repositoryUrl, String username, String password) {
		GitConfigurationSource source = new GitConfigurationSource(repositoryUrl, username, password);
		contracts.addAll(source.load());
		return this;
	}

	/**
	 * Local directory from which contracts should be loaded
	 * @param localSource
	 * @return
	 */
	public ContractClient withLocalConfig(String localSource) {
		LocalConfigurationSource source = new LocalConfigurationSource(localSource);
		contracts.addAll(source.load());
		return this;
	}

	/**
	 * List of contracts from which tests can be run
	 * @param contracts
	 * @return
	 */
	public static ContractClient fromContracts(List<Contract> contracts) {
		ContractClient contractClient = new ContractClient();
		contractClient.setContracts(contracts);
		return contractClient;
	}

	/**
	 * Filters already loaded contracts. Only contracts containing at least one tag will be retained.
	 * @param tagsToInclude
	 * @return
	 */
	public ContractClient onlyIncludeTags(String... tagsToInclude) {
		Set<Contract> contractsToInclude = new HashSet<Contract>();
		for(Contract contract : contracts) {
			Set<String> contractTags = contract.readTags();
			for(String tagToInclude : tagsToInclude) {
				if(contractTags.contains(tagToInclude)) {
					contractsToInclude.add(contract);
					continue;
				}
			}
		}
		contracts = new ArrayList<Contract>(contractsToInclude);
		return this;
	}

	/**
	 * Filters already loaded contracts. Only contracts containing none of the tagsToExclude will be retained.
	 * @param tagsToExclude
	 * @return
	 */
	public ContractClient excludeTags(String... tagsToExclude) {
		Set<Contract> contractsToExclude = new HashSet<Contract>();
		for(Contract contract : contracts) {
			Set<String> contractTags = contract.readTags();
			for(String tagToInclude : tagsToExclude) {
				if(contractTags.contains(tagToInclude)) {
					contractsToExclude.add(contract);
					continue;
				}
			}
		}
		contracts.removeAll(contractsToExclude);
		return this;
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

	public void setContracts(List<Contract> contracts) {
		this.contracts = contracts;
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
