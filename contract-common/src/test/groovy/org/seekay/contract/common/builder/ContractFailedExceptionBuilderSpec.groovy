package org.seekay.contract.common.builder

import org.seekay.contract.common.variable.VariableStore
import org.seekay.contract.model.ContractTestFixtures
import org.seekay.contract.model.domain.Contract
import org.seekay.contract.model.domain.ContractResponse
import spock.lang.Specification

class ContractFailedExceptionBuilderSpec extends Specification {

    def "an exception with a contract, response and status code error message could be built correctly" () {
        given:
            Contract contract = ContractTestFixtures.defaultGetContract().build()
            ContractResponse response = ContractTestFixtures.defaultGetContract().build().response
        when:
            def exception = ContractFailedExceptionBuilder.expectedContract(contract)
                    .actualResponse(response)
                    .statusCodes('200', '400')
                    .build()
        then:
            exception.message.contains('Status codes are expected to match, contract : 200, actual : 400')
    }

    def "an exception with a contract, response and error message should be build correctly" () {
        given:
            Contract contract = ContractTestFixtures.defaultGetContract().build()
            ContractResponse response = ContractTestFixtures.defaultGetContract().build().response
        when:
            def exception = ContractFailedExceptionBuilder.expectedContract(contract)
                    .actualResponse(response)
                    .errorMessage("I like cheese")
                    .build()
        then:
            exception.message.contains('I like cheese')
    }

    def 'variable store info should be included in the error report' () {
        given:
            Contract contract = ContractTestFixtures.defaultGetContract().build()
            ContractResponse response = ContractTestFixtures.defaultGetContract().build().response
            VariableStore variableStore = new VariableStore()
            variableStore.put("Victor","alpha")
        when:
            def exception = ContractFailedExceptionBuilder.expectedContract(contract)
                    .actualResponse(response)
                    .errorMessage("I like cheese")
                    .variableStore(variableStore)
                    .build()
        then:
            exception.message.contains('''"Victor" : "alpha"''')
    }
}
