package org.seekay.contract.client.client;

import org.seekay.contract.model.domain.Contract;
import org.seekay.contract.model.domain.ContractRequest;
import org.seekay.contract.model.domain.ContractResponse;
import org.seekay.contract.model.util.HeaderTools;
import org.seekay.contract.model.util.Http;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContractClient {

    private List<Contract> contracts;
    private String serverPath;

    private ContractClient(List<Contract> contracts) {
        this.contracts = contracts;
    }

    public static ContractClient fromContracts(List<Contract> contracts) {
        return new ContractClient(contracts);
    }

    public ContractClient toPath(String serverPath) {
        this.serverPath = serverPath;
        return this;
    }

    public void execute() {
        for(Contract contract : contracts) {
            ContractRequest request = contract.getRequest();
            ContractResponse response = Http.method(request.getMethod())
                    .toPath(serverPath + request.getPath())
                    .withHeaders(request.getHeaders())
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
        String strippedExpectedResponseBody = expectedResponse.getBody().replaceAll("\\s","");
        String strippedActualResponseBody = actualResponse.getBody().replaceAll("\\s","");
        assertThat("Response and Contract bodies are expected to match", strippedExpectedResponseBody, is(strippedActualResponseBody));
    }

    private void assertStatusCodesMatch(ContractResponse expectedResponse, ContractResponse actualResponse) {
        assertThat("Response and Contract status codes are expected to match", expectedResponse.getStatus(), is(actualResponse.getStatus()));
    }


}
