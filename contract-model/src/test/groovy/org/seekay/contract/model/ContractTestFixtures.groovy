package org.seekay.contract.model
import org.seekay.contract.model.domain.Contract

class ContractTestFixtures {

    static Set<Contract> oneDefaultContractOfEachMethod() {
        return [
                defaultGetContract().build(),
                defaultPostContract().build(),
                defaultPutContract().build(),
                defaultDeleteContract().build()
        ]
    }

    static Set<Contract> oneDefaultContractOfEachMethodWithoutHeaders() {
        return [
                defaultGetContract().requestHeaders(null).build(),
                defaultPostContract().requestHeaders(null).build(),
                defaultPutContract().requestHeaders(null).build(),
                defaultDeleteContract().requestHeaders(null).build()
        ]
    }

    static Set<Contract> multipleContractsDifferentMethodsSamePath() {
        return [
                defaultGetContract().path("/default/1").build(),
                defaultPostContract().path("/default/1").build(),
                defaultPutContract().path("/default/1").build(),
                defaultDeleteContract().path("/default/1").build()
        ]
    }

    static Set<Contract> multiplePostContractsDifferentPaths() {
        return [
                defaultPostContract().path("hello world").build(),
                defaultPostContract().path("goodbye world").build(),
                defaultPutContract().path("cruel world").build(),
                defaultDeleteContract().path("speak no evil").build()
        ]
    }


    static ContractTestBuilder defaultGetContract() {
        return ContractTestBuilder.get()
                .path("/builder/2")
                .requestHeaders(["captain":"america"])
                .responseHeaders(["captain":"america"])
                .status(200)
                .responseBody("hello world")
    }

    static ContractTestBuilder defaultPostContract() {
        return ContractTestBuilder.post()
                .path("/builder/3")
                .requestBody("I'm the request body")
                .requestHeaders(["captain":"america"])
                .status(200)
                .responseHeaders(["incredible":"hulk"])
                .responseBody("I like cheese")
    }

    static ContractTestBuilder defaultPutContract() {
        return ContractTestBuilder.put()
                .path("/builder/4")
                .requestBody("I'm the request body")
                .requestHeaders(["iron":"man"])
                .status(200)
                .responseHeaders(["war":"machine"])
                .responseBody("I like eggs")
    }

    static ContractTestBuilder defaultDeleteContract() {
        return ContractTestBuilder.delete()
                .path("/builder/5")
                .requestHeaders(["scarlet":"witch"])
                .status(204)
    }

    static ContractTestBuilder defaultOptionsContract() {
        return ContractTestBuilder.options()
                .path("/builder/6")
                .status(200)
                .responseHeaders(["allow":"OPTIONS,GET,HEAD,POST,PUT,DELETE"])
    }
}
