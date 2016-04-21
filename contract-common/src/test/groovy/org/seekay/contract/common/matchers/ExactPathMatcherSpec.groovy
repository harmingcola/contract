package org.seekay.contract.common.matchers

import org.seekay.contract.model.ContractTestFixtures
import org.seekay.contract.model.domain.Contract
import spock.lang.Shared
import spock.lang.Specification

import static org.seekay.contract.model.util.SetTools.head

class ExactPathMatcherSpec extends Specification {

    @Shared
    ExactPathMatcher matcher = new ExactPathMatcher()

    def "a single exact path match can be returned"() {
        given:
            Set<Contract> contracts = [ContractTestFixtures.defaultGetContract().build()]
        when:
            Set<Contract> result = matcher.match(contracts, "/builder/2")
        then:
            head(result).request.path == "/builder/2"
            result.size() == 1
    }

    def "if no contracts match, an empty list will be returned"() {
        given:
            Set<Contract> contracts = ContractTestFixtures.oneDefaultContractOfEachMethod()
        when:
            Set<Contract> result = matcher.match(contracts, "/I'm-an-invalid-configurePath")
        then:
            result != null
            result.size() == 0
            result.isEmpty()
    }

    def "multiple contracts with the same path can be returned"() {
        given:
            Set<Contract> contracts = ContractTestFixtures.multipleContractsDifferentMethodsSamePath()
        when:
            Set<Contract> result = matcher.match(contracts, "/default/1")
        then:
            result.size() == contracts.size()
            result.collect { contract ->
                assert contract.request.path == "/default/1"
            }
    }
}
