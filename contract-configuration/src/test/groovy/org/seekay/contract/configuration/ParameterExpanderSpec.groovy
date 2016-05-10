package org.seekay.contract.configuration

import org.seekay.contract.model.domain.Contract
import spock.lang.Shared
import spock.lang.Specification

import static org.seekay.contract.model.ContractTestFixtures.postContractWithOneParameterBlock
import static org.seekay.contract.model.tools.ListTools.first

class ParameterExpanderSpec extends Specification {

    @Shared ParameterExpander expander = new ParameterExpander()
    @Shared Contract result

    def setupSpec() {
        Contract contract = postContractWithOneParameterBlock().build()
        List contracts = expander.expandParameters(contract)
        result = first(contracts)
    }

    def 'paths should be expanded correctly' () {
        expect:
            result.request.path == '/builder/blue'
    }

    def 'request bodies should be expanded correctly' () {
        expect:
            result.request.body == 'This body contains a parameter red'
    }

    def 'request headers should be expanded correctly' () {
        expect:
            result.request.headers['captain'] == 'america'
    }

    def 'response status should be expanded correctly' () {
        expect:
            result.response.status == '200'
    }

    def 'response headers should be expanded correctly' () {
        expect:
            result.response.headers['incredible'] == '${contract.anyString}'
    }

    def 'response bodies should be expanded correctly' () {
        expect:
            result.response.body == 'Im huuuuuuge'
    }
}
