package org.seekay.contract.configuration

import org.seekay.contract.model.domain.Contract
import spock.lang.Shared
import spock.lang.Specification

import static org.seekay.contract.model.ContractTestFixtures.*
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

    def 'a parameter without a match will be replaced with an empty string' () {
        expect:
            result.request.headers['bobby'] == ''
    }

    def 'a setup block will also have parameters expanded' () {
        given:
            Contract contract = postContractWithOneParameterBlockAndASetupBlock().build()
        when:
            List contracts = expander.expandParameters(contract)
            Contract setupContract = first(contracts)
        then:
            setupContract.request.body == 'This body contains a parameter red'

    }
}
