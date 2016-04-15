package org.seekay.contract.server.service
import com.fasterxml.jackson.databind.ObjectMapper
import org.seekay.contract.common.matchers.WhiteSpaceIgnoringBodyMatcher
import org.seekay.contract.model.ContractTestFixtures
import org.seekay.contract.model.domain.Contract
import org.seekay.contract.server.match.ExactPathMatcher
import org.seekay.contract.server.match.HeaderMatcher
import org.seekay.contract.server.match.MethodMatcher
import spock.lang.Shared
import spock.lang.Specification

import static org.seekay.contract.model.domain.Method.*

class MatchingServiceOptionsSpec extends Specification{

    @Shared
    MatchingService service = new MatchingService()

    ContractService contractService = Mock(ContractService)
    MethodMatcher methodMatcher = Mock(MethodMatcher)
    ExactPathMatcher exactPathMatcher = Mock(ExactPathMatcher)
    HeaderMatcher headerMatcher = Mock(HeaderMatcher)
    WhiteSpaceIgnoringBodyMatcher whiteSpaceIgnoringBodyMatcher = Mock(WhiteSpaceIgnoringBodyMatcher)

    static def EMPTY_SET = []

    def setup() {
        service.contractService = contractService
        service.methodMatcher = methodMatcher
        service.exactPathMatcher = exactPathMatcher
        service.headerMatcher = headerMatcher
        service.whiteSpaceIgnoringBodyMatcher = whiteSpaceIgnoringBodyMatcher
        service.objectMapper = new ObjectMapper()

        contractService.read() >> {[ContractTestFixtures.defaultOptionsContract()] as Set}
    }

    def "a OPTIONS contract matching all parameters should return correctly"() {
        given:
            Contract contract = ContractTestFixtures.defaultOptionsContract().build()
            methodMatcher.match(_ as Set<Contract>, OPTIONS) >> {[contract]}
            exactPathMatcher.match(_ as Set<Contract>,_ as String) >> {[contract]}
            headerMatcher.match(_ as Set<Contract>, null) >> {[contract]}
        when:
            Contract matchedContract = service.matchOptionsRequest(contract.request)
        then:
            matchedContract.response.status == contract.response.status
            matchedContract.response.body == contract.response.body
            matchedContract.response.headers == contract.response.headers
    }

    def "a OPTIONS contract not matching method should return null"() {
        given:
            Contract contract = ContractTestFixtures.defaultOptionsContract().build()
            methodMatcher.match(_ as Set<Contract>, OPTIONS) >> {EMPTY_SET}
            exactPathMatcher.match(_ as Set<Contract>,_ as String) >> {[contract]}
            headerMatcher.match(_ as Set<Contract>, null) >> {[contract]}
        when:
            Contract matchedContract = service.matchOptionsRequest(contract.request)
        then:
            matchedContract == null
    }

    def "a OPTIONS contract not matching the path should return null"() {
        given:
            Contract contract = ContractTestFixtures.defaultOptionsContract().build()
            methodMatcher.match(_ as Set<Contract>, OPTIONS) >> {[contract]}
            exactPathMatcher.match(_ as Set<Contract>,_ as String) >> {EMPTY_SET}
            headerMatcher.match(_ as Set<Contract>, null) >> {[contract]}
        when:
            Contract matchedContract = service.matchOptionsRequest(contract.request)
        then:
            matchedContract == null
    }

    def "a OPTIONS contract not matching headers should return null"() {
        given:
            Contract contract = ContractTestFixtures.defaultOptionsContract().build()
            methodMatcher.match(_ as Set<Contract>, OPTIONS) >> {[contract]}
            exactPathMatcher.match(_ as Set<Contract>,_ as String) >> {[contract]}
            headerMatcher.match(_ as Set<Contract>, null) >> {EMPTY_SET}
        when:
            Contract matchedContract = service.matchOptionsRequest(contract.request)
        then:
            matchedContract == null
    }

    def "if multiple contracts are matched, an illegal state should be thrown" () {
        given:
            Contract contract1 = ContractTestFixtures.defaultPostContract().build()
            Contract contract2 = ContractTestFixtures.defaultOptionsContract().build()
            methodMatcher.match(_ as Set<Contract>, POST) >> {[contract1, contract2]}
            exactPathMatcher.match(_ as Set<Contract>,_ as String) >> {[contract1, contract2]}
            headerMatcher.match(_ as Set<Contract>,_ as Map<String, String>) >> {[contract1, contract2]}
        when:
            service.matchOptionsRequest(contract1.request)
        then:
            thrown IllegalStateException
    }
}
