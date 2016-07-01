package org.seekay.contract.common.variable

import org.seekay.contract.common.ApplicationContext
import org.seekay.contract.common.match.common.ExpressionMatcher
import spock.lang.Specification

import static org.seekay.contract.model.ContractTestFixtures.defaultPostContract

class VariableStoreSpec extends Specification {

    VariableStore variableStore = new VariableStore()
    ExpressionMatcher expressionMatcher = Mock(ExpressionMatcher)
    StringVariableExtractor stringVariableExtractor = Mock(StringVariableExtractor)
    JsonBodyVariableExtractor jsonBodyVariableExtractor = Mock(JsonBodyVariableExtractor)

    def setup() {
        variableStore.expressionMatcher = expressionMatcher
        variableStore.stringVariableExtractor = stringVariableExtractor
        variableStore.jsonBodyVariableExtractor = jsonBodyVariableExtractor
    }

    def 'null bodies should not call collaborators'() {
        given:
            def contract = defaultPostContract().responseBody(null).noHeaders().build()
            def actualResponse = defaultPostContract().responseBody(null).noHeaders().build().response
        when:
            variableStore.updateForResponse(contract, actualResponse)
        then:
            variableStore.size() == 0
            0 * stringVariableExtractor.extract(_ as String, _ as String)
    }

    def 'an expression match should cause an variable to be added to the store'() {
        given:
            def contract = defaultPostContract().noHeaders().build()
            def actualResponse = defaultPostContract().noHeaders().build().response
        when:
            variableStore.updateForResponse(contract, actualResponse)
        then:
            variableStore.size() == 1
            variableStore['key'] == '49'
            1 * stringVariableExtractor.extract(_ as String, _ as String) >> {return ["key":"49"]}
            1 * jsonBodyVariableExtractor.extract(_ as String, _ as String) >> {return ["key":"49"]}
    }

    def 'the variable store should be updatable from response headers' () {
        given:
            def contract = defaultPostContract().responseBody(null)
                    .responseHeaders(['key':'${contract.var.number.hello}']).build()
            def actualResponse = defaultPostContract().responseBody(null)
                    .responseHeaders(['key':'world']).build().response
        when:
            variableStore.updateForResponse(contract, actualResponse)
        then:
            variableStore.size() == 1
            variableStore['hello'] == '50'
            1 * stringVariableExtractor.extract(_ as String, _ as String) >> {return ["hello":"50"]}
            1 * jsonBodyVariableExtractor.extract(_ as String, _ as String) >> {return ["hello":"50"]}
    }

    def 'an expression match on request path should cause an variable to be added to the store'() {
        given:
            def contract = defaultPostContract().noHeaders().requestBody(null).path('/index/${contract.var.number.id}').build()
            def actualResponse = defaultPostContract().noHeaders().requestBody(null).path('/index/46').build().request
        when:
            variableStore.updateForRequest(contract, actualResponse)
        then:
            variableStore.size() == 1
            variableStore['id'] == '46'
            1 * stringVariableExtractor.extract(_ as String, _ as String) >> {return ["id":"46"]}
            1 * jsonBodyVariableExtractor.extract(_ as String, _ as String) >> {return ["id":"46"]}
    }

    def 'the variable store should be updatable from request headers' () {
        given:
            def contract = defaultPostContract().path(null).requestBody(null).requestHeaders(['key':'${contract.var.number.hello}']).build()
            def actualResponse = defaultPostContract().path(null).requestBody(null).requestHeaders(['key':'59']).build().request
        when:
            variableStore.updateForRequest(contract, actualResponse)
        then:
            variableStore.size() == 1
            variableStore['hello'] == '59'
            1 * stringVariableExtractor.extract(_ as String, _ as String) >> {return ["hello":"59"]}
            1 * jsonBodyVariableExtractor.extract(_ as String, _ as String) >> {return ["hello":"59"]}
    }

    /*
     * Integration tests
     */

    def 'failure from contract tools acceptance 001'() {
        given:
            VariableStore variableStore = ApplicationContext.variableStore()
            def contractString = '{"id":"${contract.var.string.repoId}","name":"kvServerContracts","url":"https://github.com/harmingcola/kvServerContracts.git"}'
            def actualString = '{"id":"b6697967-db3a-4ce9-a7bd-ac86c1da9dfc","url":"https://github.com/harmingcola/kvServerContracts.git","name":"kvServerContracts","username":null,"password":null}'
        when:
            variableStore.updateFromStrings(contractString, actualString)
        then:
            variableStore['repoId'] == 'b6697967-db3a-4ce9-a7bd-ac86c1da9dfc'
    }

    def 'failure from contract tools acceptance 002'() {
        given:
            VariableStore variableStore = ApplicationContext.variableStore()
            def contractString = '[{"id":"${contract.var.string.repoId}","name":"kvServerContracts","url":"https://github.com/harmingcola/kvServerContracts.git"}]'
            def actualString = '[{"id":"8feb069d-a68e-4a2d-8f8d-cc01925988e2","url":"https://github.com/harmingcola/kvServerContracts.git","name":"kvServerContracts","username":null,"password":null}]'
        when:
            variableStore.updateFromStrings(contractString, actualString)
        then:
            variableStore['repoId'] == '8feb069d-a68e-4a2d-8f8d-cc01925988e2'
    }
}
