package org.seekay.contract.client.client;

import lombok.extern.slf4j.Slf4j;
import org.seekay.contract.configuration.git.GitConfigurationSource;
import org.seekay.contract.model.domain.Contract;
import org.seekay.contract.model.domain.ContractRequest;
import org.seekay.contract.model.domain.ContractResponse;
import org.seekay.contract.model.util.HeaderTools;
import org.seekay.contract.model.util.Http;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@Slf4j
public class ContractClient {

    private List<Contract> contracts;
    private String path;

    private ContractClient(List<Contract> contracts) {
        this.contracts = contracts;
    }

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
		return new ContractClient(contracts);
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

    private void assertResponseIsValid(ContractResponse expectedResponse, ContractResponse actualResponse) {
        assertStatusCodesMatch(expectedResponse, actualResponse);
        assertBodiesMatch(expectedResponse, actualResponse);
        assertHeadersContained(expectedResponse, actualResponse);
    }

    private void assertHeadersContained(ContractResponse expectedResponse, ContractResponse actualResponse) {
        boolean headersContained = HeaderTools.isSubMap(expectedResponse.getHeaders(), actualResponse.getHeaders());
        assertThat("Response headers are expected to contain all Contract Headers", headersContained, is(true));
    }

    private void assertBodiesMatch(ContractResponse expectedResponse, ContractResponse actualResponse) {
        if(expectedResponse.getBody() == null) {
            return;
        }
        String strippedExpectedResponseBody = expectedResponse.getBody().replaceAll("\\s","");
        String strippedActualResponseBody = actualResponse.getBody().replaceAll("\\s","");
        assertThat("Response and Contract bodies are expected to match", strippedExpectedResponseBody, is(strippedActualResponseBody));
    }

    private void assertStatusCodesMatch(ContractResponse expectedResponse, ContractResponse actualResponse) {
        assertThat("Response and Contract status codes are expected to match", actualResponse.getStatus(), is(expectedResponse.getStatus()));
    }


}

