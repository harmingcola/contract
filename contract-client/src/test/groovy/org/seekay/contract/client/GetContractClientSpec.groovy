package org.seekay.contract.client
import org.seekay.contract.client.client.ContractClient
import org.seekay.contract.model.domain.Contract
import org.seekay.contract.server.ClientFacingTest

import static org.seekay.contract.model.ContractTestFixtures.defaultGetContract

class GetContractClientSpec extends ClientFacingTest {

    def "The client will assert the correct response on a get request" () {
        given:
            Contract contract = defaultGetContract().path("/ClientGetRequestStatusSpec/1").build()
            server.addContract(contract)
        expect:
            ContractClient.fromContracts([contract]).againstPath(server.path()).runTests()
    }

    def "The client will throw an exception when the incorrect status code is returned" () {
        given:
            Contract clientContract = defaultGetContract().path("/ClientGetRequestStatusSpec/2").status(200).build()
            Contract serverContract = defaultGetContract().path("/ClientGetRequestStatusSpec/2").status(201).build()
            server.addContract(serverContract)
        when:
            ContractClient.fromContracts([clientContract]).againstPath(server.path()).runTests()
        then:
            def e = thrown(AssertionError)
            e.message.contains("Response and Contract status codes are expected to match")
    }

    def "The client will throw an exception when the returned body doesn't match the expected" () {
        given:
            Contract clientContract = defaultGetContract().path("/ClientGetRequestStatusSpec/3").responseBody("I'm a text body").build()
            Contract serverContract = defaultGetContract().path("/ClientGetRequestStatusSpec/3").responseBody("This wont match").build()
            server.addContract(serverContract)
        when:
            ContractClient.fromContracts([clientContract]).againstPath(server.path()).runTests()
        then:
            def e = thrown(AssertionError)
            e.message.contains("Response and Contract bodies are expected to match")
    }

    def "a server and client configured from identical sources should operate correctly" () {
        given:
            server.withGitConfig("https://bitbucket.org/harmingcola/contract-test-public")
                .addContractsFromConfigSources()
        when:
            ContractClient.newClient()
                .withGitConfig("https://bitbucket.org/harmingcola/contract-test-public")
                .againstPath(server.path())
                .runTests()
        then:
            noExceptionThrown()
    }

    /*
     * possible bug?
     */
    def "contracts with identical bodies, except for whitespace will match and not throw an exception" () {
        given:
            Contract clientContract = defaultGetContract().path("/ClientGetRequestStatusSpec/4").responseBody("I'm a text body").build()
            Contract serverContract = defaultGetContract().path("/ClientGetRequestStatusSpec/4").responseBody("I'matextbody").build()
            server.addContract(serverContract)
        expect:
            ContractClient.fromContracts([clientContract]).againstPath(server.path()).runTests()
    }

    def "a client should load contracts from a secured git repository" () {
        given:
            def contracts = ContractClient.newClient().withGitConfig("https://bitbucket.org/harmingcola/contract-test-private.git", 'seekay_test', 'seekay_test_password').contracts
        expect:
            contracts.size() == 1
    }

    def "a client should load contracts from a local source" () {
        given:
            def contracts = ContractClient.newClient().withLocalConfig('src/test/resources/contracts').contracts
        expect:
            contracts.size() == 1
    }
}
