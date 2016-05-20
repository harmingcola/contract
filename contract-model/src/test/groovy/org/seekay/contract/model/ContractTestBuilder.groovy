package org.seekay.contract.model

import org.seekay.contract.model.domain.Contract
import org.seekay.contract.model.domain.ContractRequest
import org.seekay.contract.model.domain.ContractResponse
import org.seekay.contract.model.domain.Method

import static org.seekay.contract.model.domain.Method.*

class ContractTestBuilder {

    Method method
    String path
    String requestBody
    Map<String, String> requestHeaders

    String status
    String responseBody
    Map<String, String> responseHeaders

	Map<String, Object> info = [] as HashMap
	Set<String> tags = []
    List<Map<String, String>> parameters
    List<Contract> setup


	private ContractTestBuilder(Method method) {
        this.method = method
    }

    /*
     * Methods
     */
    static ContractTestBuilder get() {
        return new ContractTestBuilder(GET)
    }

    static ContractTestBuilder post() {
        return new ContractTestBuilder(POST)
    }

    static ContractTestBuilder put() {
        return new ContractTestBuilder(PUT)
    }

    static ContractTestBuilder delete() {
        return new ContractTestBuilder(DELETE)
    }


    /*
     * Parameter methods
     */
    ContractTestBuilder path(String path) {
        this.path = path
        return this
    }

    ContractTestBuilder requestHeaders(Map<String, String> headers) {
        this.requestHeaders = headers
        return this
    }

    ContractTestBuilder noRequestHeaders() {
        requestHeaders = null
        return this
    }

    def noHeaders() {
        this.responseHeaders = null
        this.requestHeaders = null
        return this
    }

    ContractTestBuilder responseHeaders(Map<String, String> responseHeaders) {
        this.responseHeaders = responseHeaders
        return this
    }

    ContractTestBuilder status(String status) {
        this.status = status
        return this
    }

    ContractTestBuilder responseBody(String responseBody) {
        this.responseBody = responseBody
        return this
    }

    ContractTestBuilder requestBody(String requestBody) {
        this.requestBody = requestBody
        return this
    }

	ContractTestBuilder tags(String... tags) {
		this.tags = Arrays.asList(tags)
		return this
	}

    ContractTestBuilder parameters(List<Map<String, String>> parameters) {
        this.parameters = parameters
        return this
    }

    ContractTestBuilder setup(Contract setupContract) {
        if(setup == null) {
            setup = new LinkedList<>()
        }
        setup.add(setupContract)
        return this
    }


    /*
     * Builder
     */
    Contract build() {
        return new Contract(
				info: [
					tags: tags.asList()
				],
                request: new ContractRequest(
                        method: method,
                        path: path,
                        body: requestBody,
                        headers: requestHeaders
                ),
                response: new ContractResponse(
                        status: status,
                        body: responseBody,
                        headers: responseHeaders
                ),
                parameters: parameters,
                setup: setup
        )
    }

}
