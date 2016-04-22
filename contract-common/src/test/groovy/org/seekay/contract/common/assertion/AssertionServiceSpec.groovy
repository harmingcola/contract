package org.seekay.contract.common.assertion

import org.seekay.contract.model.ContractTestFixtures
import org.seekay.contract.model.domain.ContractResponse
import spock.lang.Specification

class AssertionServiceSpec extends Specification {

    Asserter uglyAsserter = Mock(Asserter)
    Asserter prettyAsserter = Mock(Asserter)

    AssertionService service = new AssertionService(
            asserters: [
                    uglyAsserter, prettyAsserter
            ] as LinkedHashSet
    )


    def 'a call to the service should be forwarded to each of the asserters' () {
        given:
            ContractResponse contractResponse = ContractTestFixtures.defaultGetContract().build().response
            ContractResponse actualResponse = ContractTestFixtures.defaultGetContract().build().response
        when:
            service.assertOnWildCards(contractResponse, actualResponse)
        then:
            1 * uglyAsserter.assertOnWildCards(_,_)
            1 * prettyAsserter.assertOnWildCards(_,_)
    }
}
