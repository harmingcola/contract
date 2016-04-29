package org.seekay.contract.common.match
import com.fasterxml.jackson.databind.ObjectMapper
import org.seekay.contract.common.match.body.BodyMatchingService
import org.seekay.contract.common.match.path.PathMatchingService
import org.seekay.contract.common.matchers.HeaderMatcher
import org.seekay.contract.common.matchers.MethodMatcher
import org.seekay.contract.common.service.ContractService
import org.seekay.contract.model.ContractTestFixtures
import org.seekay.contract.model.domain.Contract
import org.seekay.contract.model.domain.ContractRequest
import spock.lang.Shared
import spock.lang.Specification

import static org.seekay.contract.model.domain.Method.POST

class MatchingServicePostSpec extends Specification{

    @Shared
    MatchingService service = new MatchingService()

    ContractService contractService = Mock(ContractService)
    MethodMatcher methodMatcher = Mock(MethodMatcher)
    PathMatchingService pathMatchingService = Mock(PathMatchingService)
    HeaderMatcher headerMatcher = Mock(HeaderMatcher)
    BodyMatchingService bodyMatchService = Mock(BodyMatchingService)


    static def EMPTY_SET = []

    def setup() {
        service.contractService = contractService
        service.methodMatcher = methodMatcher
        service.pathMatchingService = pathMatchingService
        service.headerMatcher = headerMatcher
        service.bodyMatchingService = bodyMatchService
        service.objectMapper = new ObjectMapper()

        contractService.read() >> {[ContractTestFixtures.defaultGetContract()] as Set}
    }

    def "a POST contract matching all parameters should return correctly"() {
        given:
            Contract contract = ContractTestFixtures.defaultPostContract().build()
            methodMatcher.findMatches(_ as Set<Contract>, POST) >> {[contract]}
            pathMatchingService.findMatches(_ as Set<Contract>,_ as String) >> {[contract]}
            headerMatcher.isMatch(_ as Set<Contract>,_ as Map<String, String>) >> {[contract]}
            bodyMatchService.findMatches(_ as Set<Contract>,_ as ContractRequest) >> {[contract]}
        when:
            Contract matchedContract = service.matchPostRequest(contract.request)
        then:
            matchedContract.response.status == contract.response.status
            matchedContract.response.body == contract.response.body
            matchedContract.response.headers == contract.response.headers
    }

    def "a POST contract not matching method should return null"() {
        given:
            Contract contract = ContractTestFixtures.defaultPostContract().build()
            methodMatcher.findMatches(_ as Set<Contract>, POST) >> {EMPTY_SET}
            pathMatchingService.findMatches(_ as Set<Contract>,_ as String) >> {[contract]}
            headerMatcher.isMatch(_ as Set<Contract>,_ as Map<String, String>) >> {[contract]}
            bodyMatchService.findMatches(_ as Set<Contract>,_ as ContractRequest) >> {[contract]}
        when:
            Contract matchedContract = service.matchPostRequest(contract.request)
        then:
            matchedContract == null
    }

    def "a POST contract not matching the path should return null"() {
        given:
            Contract contract = ContractTestFixtures.defaultPostContract().build()
            methodMatcher.findMatches(_ as Set<Contract>, POST) >> {[contract]}
            pathMatchingService.findMatches(_ as Set<Contract>,_ as String) >> {EMPTY_SET}
            headerMatcher.isMatch(_ as Set<Contract>,_ as Map<String, String>) >> {[contract]}
            bodyMatchService.findMatches(_ as Set<Contract>,_ as ContractRequest) >> {[contract]}
        when:
            Contract matchedContract = service.matchPostRequest(contract.request)
        then:
            matchedContract == null
    }

    def "a POST contract not matching headers should return null"() {
        given:
            Contract contract = ContractTestFixtures.defaultPostContract().build()
            methodMatcher.findMatches(_ as Set<Contract>, POST) >> {[contract]}
            pathMatchingService.findMatches(_ as Set<Contract>,_ as String) >> {[contract]}
            headerMatcher.isMatch(_ as Set<Contract>,_ as Map<String, String>) >> {EMPTY_SET}
            bodyMatchService.findMatches(_ as Set<Contract>,_ as ContractRequest) >> {[contract]}
        when:
            Contract matchedContract = service.matchPostRequest(contract.request)
        then:
            matchedContract == null
    }

    def "a POST contract not matching the body should return null"() {
        given:
            Contract contract = ContractTestFixtures.defaultPostContract().build()
            methodMatcher.findMatches(_ as Set<Contract>, POST) >> {[contract]}
            pathMatchingService.findMatches(_ as Set<Contract>,_ as String) >> {[contract]}
            headerMatcher.isMatch(_ as Set<Contract>,_ as Map<String, String>) >> {[contract]}
            bodyMatchService.findMatches(_ as Set<Contract>,_ as ContractRequest) >> {EMPTY_SET}
        when:
            Contract matchedContract = service.matchPostRequest(contract.request)
        then:
            matchedContract == null
    }

    def "if multiple contracts are matched, an illegal state should be thrown" () {
        given:
            Contract contract1 = ContractTestFixtures.defaultPostContract().build()
            Contract contract2 = ContractTestFixtures.defaultGetContract().build()
            methodMatcher.findMatches(_ as Set<Contract>, POST) >> {[contract1, contract2]}
            pathMatchingService.findMatches(_ as Set<Contract>,_ as String) >> {[contract1, contract2]}
            headerMatcher.isMatch(_ as Set<Contract>,_ as Map<String, String>) >> {[contract1, contract2]}
            bodyMatchService.findMatches(_ as Set<Contract>,_ as ContractRequest) >> {[contract1, contract2]}
        when:
            service.matchPostRequest(contract1.request)
        then:
            thrown IllegalStateException
    }
}
