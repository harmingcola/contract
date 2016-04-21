package org.seekay.contract.common.matchers

import org.seekay.contract.common.matchers.MethodMatcher
import org.seekay.contract.model.ContractTestFixtures
import spock.lang.Shared
import spock.lang.Specification

import static org.seekay.contract.model.domain.Method.GET
import static org.seekay.contract.model.domain.Method.POST
import static org.seekay.contract.model.ContractTestFixtures.defaultGetContract
import static org.seekay.contract.model.util.SetTools.head

class MethodMatcherSpec extends Specification {

    @Shared
    MethodMatcher matcher = new MethodMatcher();

    def "only a contract matching the correct method should be returned"() {
        given:
            Set contracts = [defaultGetContract().build()]
        when:
            Set result = matcher.findMatches(contracts, GET)
        then:
            result.size() == 1
            head(result).request.method.equals(GET)
    }

    def "if no contracts match, an empty list will be returned"() {
        given:
            Set contracts = ContractTestFixtures.multiplePostContractsDifferentPaths()
        when:
            Set result = matcher.findMatches(contracts, GET)
        then:
            result != null
            result.size() == 0
            result.isEmpty()
    }

    def "if multiple contracts match, then multiple contracts will be returned" () {
        given:
            Set contracts = ContractTestFixtures.multiplePostContractsDifferentPaths()
        when:
            Set result = matcher.findMatches(contracts, POST)
        then:
            result.size() == 2
            result.collect { contract ->
                assert contract.request.method == POST
            }
    }

}
