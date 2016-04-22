package org.seekay.contract.client.client;

import lombok.extern.slf4j.Slf4j;
import org.seekay.contract.common.ApplicationContext;
import org.seekay.contract.common.assertion.AssertionService;
import org.seekay.contract.common.match.body.BodyMatchService;
import org.seekay.contract.common.matchers.HeaderMatcher;
import org.seekay.contract.configuration.git.GitConfigurationSource;
import org.seekay.contract.model.domain.Contract;
import org.seekay.contract.model.domain.ContractRequest;
import org.seekay.contract.model.domain.ContractResponse;
import org.seekay.contract.model.util.Http;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@Slf4j
public class ContractClient {

    private List<Contract> contracts;
    private String path;

    private HeaderMatcher headerMatcher = ApplicationContext.headerMatcher();
    private BodyMatchService bodyMatchService = ApplicationContext.bodyMatchService();
    private AssertionService assertionService = ApplicationContext.assertionService();

    private ContractClient() {
        contracts = new ArrayList<Contract>();
    }

	public static ContractClient newClient() {
		return new ContractClient();
	}

    public ContractClient againstPath(String path) {
        this.path = path;
        return this;
    }

	public ContractClient withGitConfig(String repositoryUrl) {
		GitConfigurationSource source = new GitConfigurationSource(repositoryUrl);
		contracts.addAll(source.load());
		return this;
	}

	public static ContractClient fromContracts(List<Contract> contracts) {
        ContractClient contractClient = new ContractClient();
        contractClient.setContracts(contracts);
        return contractClient;
	}

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }

    public void runTests() {
        for(Contract contract : contracts) {
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
        boolean bodiesMatch = bodyMatchService.isMatch(contractResponse.getBody(), actualResponse.getBody());
        assertThat("Response and Contract bodies are expected to match", bodiesMatch, is(true));
    }

    private void assertStatusCodesMatch(ContractResponse contractResponse, ContractResponse actualResponse) {
        assertThat("Response and Contract status codes are expected to match", actualResponse.getStatus(), is(contractResponse.getStatus()));
    }

}
