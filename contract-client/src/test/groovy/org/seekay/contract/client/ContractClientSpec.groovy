package org.seekay.contract.client
import org.seekay.contract.client.client.ContractClient
import org.seekay.contract.model.domain.Contract
import org.seekay.contract.model.exception.ContractFailedException
import org.seekay.contract.server.ClientFacingTest

import static org.seekay.contract.model.ContractTestFixtures.defaultGetContract

class ContractClientSpec extends ClientFacingTest {

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
            def e = thrown(ContractFailedException)
            e.message.contains("Status codes are expected to match")
    }

    def "The client will throw an exception when the returned body doesn't match the expected" () {
        given:
            Contract clientContract = defaultGetContract().path("/ClientGetRequestStatusSpec/3").responseBody("I'm a text body").build()
            Contract serverContract = defaultGetContract().path("/ClientGetRequestStatusSpec/3").responseBody("This wont match").build()
            server.addContract(serverContract)
        when:
            ContractClient.fromContracts([clientContract]).againstPath(server.path()).runTests()
        then:
            def e = thrown(ContractFailedException)
            e.message.contains("Bodies are expected to match")
    }

    def "The client will throw an exception when the returned headers doesn't contain everything from the contract" () {
        given:
            Contract serverContract = defaultGetContract().responseHeaders(["hello":"world"]).build()
            Contract clientContract = defaultGetContract().responseHeaders(["booo":"ya"]).build()
            server.addContract(serverContract)
        when:
            ContractClient.fromContracts([clientContract]).againstPath(server.path()).runTests()
        then:
            def e = thrown(ContractFailedException)
            e.message.contains("Response headers are expected to contain all Contract Headers")
    }

    def "a server and client configured from identical sources should operate correctly" () {
        given:
            server.withGitConfig("https://bitbucket.org/harmingcola/contract-test-public")
                .pushContractsToServer()
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

	def "a client should be able to run contracts with certain tags" () {
		given:
			def contracts = [
			        defaultGetContract().tags("one", "delete").build(),
					defaultGetContract().tags("two", "delete").build(),
					defaultGetContract().tags("three", "delete").build()
			]
			def contractClient = ContractClient.fromContracts(contracts)
		when:
			contractClient.retainTags("three")
		then:
			contractClient.contracts.size() == 1
	}

	def "a client should not run contracts with certain tags" () {
		given:
			def contracts = [
					defaultGetContract().tags("one", "delete").build(),
					defaultGetContract().tags("two", "delete").build(),
					defaultGetContract().tags("three", "delete").build()
			]
			def contractClient = ContractClient.fromContracts(contracts)
		when:
			contractClient.excludeTags("two")
		then:
			contractClient.contracts.size() == 2
	}

    def "a client should be able to both include and exclude features with one call" () {
        given:
            def contracts = [
                defaultGetContract().tags("one", "delete").build(),
                defaultGetContract().tags("two", "delete").build(),
                defaultGetContract().tags("three", "delete").build(),
                defaultGetContract().tags("four", "get").build()
            ]
            def contractClient = ContractClient.fromContracts(contracts)
        when:
            contractClient.tags(["delete"] as Set, ["one", "three"] as Set)
        then:
            contractClient.contracts.size() == 1
    }

    def "Multiple contracts should be addable with a single call" () {
        given:
            ContractClient contractClient = ContractClient.newClient()
        when:
            contractClient.addContracts(
                    defaultGetContract().build(),
                    defaultGetContract().build()
            )
        then:
            contractClient.contracts.size() == 2
    }

    def "Single contracts should be addable with a single call" () {
        given:
            ContractClient contractClient = ContractClient.newClient()
        when:
            contractClient.addContract(defaultGetContract().build())
        then:
            contractClient.contracts.size() == 1
    }

    def "a client can be configured with multiple local configurations" () {
        given:
            ContractClient contractClient = ContractClient.newClient()
        when:
            contractClient.withLocalConfig(
                    "src/test/resources/contracts",
                    "src/test/resources/contracts"
            )
        then:
            contractClient.contracts.size() == 2
    }
}
