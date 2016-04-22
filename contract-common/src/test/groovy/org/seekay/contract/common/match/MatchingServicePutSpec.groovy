package org.seekay.contract.common.match
import com.fasterxml.jackson.databind.ObjectMapper
import org.seekay.contract.common.match.body.BodyMatchService
import org.seekay.contract.common.matchers.ExactPathMatcher
import org.seekay.contract.common.matchers.HeaderMatcher
import org.seekay.contract.common.matchers.MethodMatcher
import org.seekay.contract.common.service.ContractService
import org.seekay.contract.model.ContractTestFixtures
import org.seekay.contract.model.domain.Contract
import spock.lang.Shared
import spock.lang.Specification

import static org.seekay.contract.model.domain.Method.PUT

class MatchingServicePutSpec extends Specification{

    @Shared
    MatchingService service = new MatchingService()

    ContractService contractService = Mock(ContractService)
    MethodMatcher methodMatcher = Mock(MethodMatcher)
    ExactPathMatcher exactPathMatcher = Mock(ExactPathMatcher)
    HeaderMatcher headerMatcher = Mock(HeaderMatcher)
    BodyMatchService bodyMatchService = Mock(BodyMatchService)


    static def EMPTY_SET = []

    def setup() {
        service.contractService = contractService
        service.methodMatcher = methodMatcher
        service.exactPathMatcher = exactPathMatcher
        service.headerMatcher = headerMatcher
        service.bodyMatchService = bodyMatchService
        service.objectMapper = new ObjectMapper()

        contractService.read() >> {[ContractTestFixtures.defaultGetContract()] as Set}
    }

    def "a PUT contract matching all parameters should return correctly"() {
        given:
            Contract contract = ContractTestFixtures.defaultPutContract().build()
            methodMatcher.findMatches(_ as Set<Contract>, PUT) >> {[contract]}
            exactPathMatcher.match(_ as Set<Contract>,_ as String) >> {[contract]}
            headerMatcher.isMatch(_ as Set<Contract>,_ as Map<String, String>) >> {[contract]}
            bodyMatchService.findMatches(_ as Set<Contract>,_ as String) >> {[contract]}
        when:
            Contract matchedContract = service.matchPutRequest(contract.request)
        then:
            matchedContract.response.status == contract.response.status
            matchedContract.response.body == contract.response.body
            matchedContract.response.headers == contract.response.headers
    }

    def "a PUT contract not matching method should return null"() {
        given:
            Contract contract = ContractTestFixtures.defaultPutContract().build()
            methodMatcher.findMatches(_ as Set<Contract>, PUT) >> {EMPTY_SET}
            exactPathMatcher.match(_ as Set<Contract>,_ as String) >> {[contract]}
            headerMatcher.isMatch(_ as Set<Contract>,_ as Map<String, String>) >> {[contract]}
            bodyMatchService.findMatches(_ as Set<Contract>,_ as String) >> {[contract]}
        when:
            Contract matchedContract = service.matchPutRequest(contract.request)
        then:
            matchedContract == null
    }

    def "a PUT contract not matching the path should return null"() {
        given:
            Contract contract = ContractTestFixtures.defaultPutContract().build()
            methodMatcher.findMatches(_ as Set<Contract>, PUT) >> {[contract]}
            exactPathMatcher.match(_ as Set<Contract>,_ as String) >> {EMPTY_SET}
            headerMatcher.isMatch(_ as Set<Contract>,_ as Map<String, String>) >> {[contract]}
            bodyMatchService.findMatches(_ as Set<Contract>,_ as String) >> {[contract]}
        when:
            Contract matchedContract = service.matchPutRequest(contract.request)
        then:
            matchedContract == null
    }

    def "a PUT contract not matching headers should return null"() {
        given:
            Contract contract = ContractTestFixtures.defaultPutContract().build()
            methodMatcher.findMatches(_ as Set<Contract>, PUT) >> {[contract]}
            exactPathMatcher.match(_ as Set<Contract>,_ as String) >> {[contract]}
            headerMatcher.isMatch(_ as Set<Contract>,_ as Map<String, String>) >> {EMPTY_SET}
            bodyMatchService.findMatches(_ as Set<Contract>,_ as String) >> {[contract]}
        when:
            Contract matchedContract = service.matchPutRequest(contract.request)
        then:
            matchedContract == null
    }

    def "a PUT contract not matching the body should return null"() {
        given:
            Contract contract = ContractTestFixtures.defaultPutContract().build()
            methodMatcher.findMatches(_ as Set<Contract>, PUT) >> {[contract]}
            exactPathMatcher.match(_ as Set<Contract>,_ as String) >> {[contract]}
            headerMatcher.isMatch(_ as Set<Contract>,_ as Map<String, String>) >> {[contract]}
            bodyMatchService.findMatches(_ as Set<Contract>,_ as String) >> {EMPTY_SET}
        when:
            Contract matchedContract = service.matchPutRequest(contract.request)
        then:
            matchedContract == null
    }

    def "if multiple contracts are matched, an illegal state should be thrown" () {
        given:
            Contract contract1 = ContractTestFixtures.defaultPutContract().build()
            Contract contract2 = ContractTestFixtures.defaultGetContract().build()
            methodMatcher.findMatches(_ as Set<Contract>, PUT) >> {[contract1, contract2]}
            exactPathMatcher.match(_ as Set<Contract>,_ as String) >> {[contract1, contract2]}
            headerMatcher.isMatch(_ as Set<Contract>,_ as Map<String, String>) >> {[contract1, contract2]}
            bodyMatchService.findMatches(_ as Set<Contract>,_ as String) >> {[contract1, contract2]}
        when:
            service.matchPutRequest(contract1.request)
        then:
            thrown IllegalStateException
    }
}
