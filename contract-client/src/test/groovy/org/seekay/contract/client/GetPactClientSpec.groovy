package org.seekay.contract.client

import org.seekay.contract.client.client.ContractClient
import org.seekay.contract.model.domain.Contract
import org.seekay.contract.server.ContractServer
import spock.lang.Specification

import static org.seekay.contract.model.ContractTestFixtures.defaultGetContract

class GetContractClientSpec extends Specification {

    def "The client will assert the correct response on a get request" () {
        given:
            Contract contract = defaultGetContract().path("/ClientGetRequestStatusSpec/1").build()
            ContractServer server = ContractServer.newServer().onRandomPort().startServer()
            server.addContract(contract)
        expect:
            ContractClient.fromContracts([contract]).toPath(server.path()).execute()
        cleanup:
            server.stopServer()
    }

    def "The client will throw an exception when the incorrect status code is returned" () {
        given:
            Contract clientContract = defaultGetContract().path("/ClientGetRequestStatusSpec/2").status(200).build()
            Contract serverContract = defaultGetContract().path("/ClientGetRequestStatusSpec/2").status(201).build()
            ContractServer server = ContractServer.newServer().onRandomPort().startServer()
            server.addContract(serverContract)
        when:
            ContractClient.fromContracts([clientContract]).toPath(server.path()).execute()
        then:
            def e = thrown(AssertionError)
            e.message.contains("Response and Contract status codes are expected to match")
        cleanup:
            server.stopServer()
    }

    def "The client will throw an exception when the returned body doesn't match the expected" () {
        given:
            Contract clientContract = defaultGetContract().path("/ClientGetRequestStatusSpec/3").responseBody("I'm a text body").build()
            Contract serverContract = defaultGetContract().path("/ClientGetRequestStatusSpec/3").responseBody("This wont match").build()
            ContractServer server = ContractServer.newServer().onRandomPort().startServer()
            server.addContract(serverContract)
        when:
            ContractClient.fromContracts([clientContract]).toPath(server.path()).execute()
        then:
            def e = thrown(AssertionError)
            e.message.contains("Response and Contract bodies are expected to match")
        cleanup:
            server.stopServer()
    }

    /*
     * possible bug?
     */
    def "contracts with identical bodies, except for whitespace will match and not throw an exception" () {
        given:
            Contract clientContract = defaultGetContract().path("/ClientGetRequestStatusSpec/4").responseBody("I'm a text body").build()
            Contract serverContract = defaultGetContract().path("/ClientGetRequestStatusSpec/4").responseBody("I'matextbody").build()
            ContractServer server = ContractServer.newServer().onRandomPort().startServer()
            server.addContract(serverContract)
        expect:
            ContractClient.fromContracts([clientContract]).toPath(server.path()).execute()
        cleanup:
            server.stopServer()
    }
}
