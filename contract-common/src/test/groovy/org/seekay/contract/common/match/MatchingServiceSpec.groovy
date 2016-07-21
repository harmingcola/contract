package org.seekay.contract.common.match
import com.fasterxml.jackson.databind.ObjectMapper
import org.seekay.contract.common.match.body.BodyMatchingService
import org.seekay.contract.common.match.path.PathMatchingService
import org.seekay.contract.common.matchers.HeaderMatcher
import org.seekay.contract.common.matchers.MethodMatcher
import org.seekay.contract.common.service.ContractService
import org.seekay.contract.common.variable.VariableStore
import org.seekay.contract.model.ContractTestFixtures
import org.seekay.contract.model.domain.Contract
import org.seekay.contract.model.domain.ContractRequest
import spock.lang.Shared
import spock.lang.Specification

import static org.seekay.contract.model.domain.Method.*

class MatchingServiceSpec extends Specification{

    @Shared
    MatchingService service = new MatchingService()

    ContractService contractService = Mock(ContractService)
    MethodMatcher methodMatcher = Mock(MethodMatcher)
    PathMatchingService pathMatchingService = Mock(PathMatchingService)
    HeaderMatcher headerMatcher = Mock(HeaderMatcher)
    BodyMatchingService bodyMatchService = Mock(BodyMatchingService)
    VariableStore variableStore = Mock(VariableStore)

    static def EMPTY_SET = []

    def setup() {
        service.contractService = contractService
        service.methodMatcher = methodMatcher
        service.pathMatchingService = pathMatchingService
        service.headerMatcher = headerMatcher
        service.bodyMatchingService = bodyMatchService
        service.variableStore = variableStore
        service.objectMapper = new ObjectMapper()

        contractService.readEnabled() >> {[ContractTestFixtures.oneDefaultContractOfEachMethod()] as Set}
    }

    def "a get contract matching all parameters should return correctly"() {
        given:
            Contract contract = ContractTestFixtures.defaultGetContract().build()
            methodMatcher.findMatches(_ as Set<Contract>, GET) >> {[contract]}
            pathMatchingService.findMatches(_ as Set<Contract>,_ as String) >> {[contract]}
            headerMatcher.isMatch(_ as Set<Contract>,_ as Map<String, String>) >> {[contract]}
        when:
            Contract matchedContract = service.matchGetRequest(contract.request)
        then:
            matchedContract.response.status == contract.response.status
            matchedContract.response.body == contract.response.body
            matchedContract.response.headers == contract.response.headers
    }

    def "a post contract matching all parameters should return correctly"() {
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

    def "a put contract matching all parameters should return correctly"() {
        given:
            Contract contract = ContractTestFixtures.defaultPutContract().build()
            methodMatcher.findMatches(_ as Set<Contract>, PUT) >> {[contract]}
            pathMatchingService.findMatches(_ as Set<Contract>,_ as String) >> {[contract]}
            headerMatcher.isMatch(_ as Set<Contract>,_ as Map<String, String>) >> {[contract]}
            bodyMatchService.findMatches(_ as Set<Contract>,_ as ContractRequest) >> {[contract]}
        when:
            Contract matchedContract = service.matchPutRequest(contract.request)
        then:
            matchedContract.response.status == contract.response.status
            matchedContract.response.body == contract.response.body
            matchedContract.response.headers == contract.response.headers
    }

    def "a delete contract matching all parameters should return correctly"() {
        given:
            Contract contract = ContractTestFixtures.defaultDeleteContract().build()
            methodMatcher.findMatches(_ as Set<Contract>, DELETE) >> {[contract]}
            pathMatchingService.findMatches(_ as Set<Contract>,_ as String) >> {[contract]}
            headerMatcher.isMatch(_ as Set<Contract>,_ as Map<String, String>) >> {[contract]}
            bodyMatchService.findMatches(_ as Set<Contract>,_ as ContractRequest) >> {[contract]}
        when:
            Contract matchedContract = service.matchDeleteRequest(contract.request)
        then:
            matchedContract.response.status == contract.response.status
            matchedContract.response.body == contract.response.body
            matchedContract.response.headers == contract.response.headers
    }

    def "a head contract matching all parameters should return correctly"() {
        given:
            Contract contract = ContractTestFixtures.defaultHeadContract().build()
            methodMatcher.findMatches(_ as Set<Contract>, HEAD) >> {[contract]}
            pathMatchingService.findMatches(_ as Set<Contract>, null) >> {[contract]}
            headerMatcher.isMatch(_ as Set<Contract>, null) >> {[contract]}
            bodyMatchService.findMatches(_ as Set<Contract>,_ as ContractRequest) >> {[contract]}
        when:
            Contract matchedContract = service.matchHeadRequest(contract.request)
        then:
            matchedContract.response.status == contract.response.status
            matchedContract.response.body == contract.response.body
            matchedContract.response.headers == contract.response.headers
    }

    def "an options contract matching all parameters should return correctly"() {
        given:
            Contract contract = ContractTestFixtures.defaultOptionsContract().build()
            methodMatcher.findMatches(_ as Set<Contract>, OPTIONS) >> {[contract]}
            pathMatchingService.findMatches(_ as Set<Contract>, null) >> {[contract]}
            headerMatcher.isMatch(_ as Set<Contract>, null) >> {[contract]}
            bodyMatchService.findMatches(_ as Set<Contract>,_ as ContractRequest) >> {[contract]}
        when:
            Contract matchedContract = service.matchOptionsRequest(contract.request)
        then:
            matchedContract.response.status == contract.response.status
            matchedContract.response.body == contract.response.body
            matchedContract.response.headers == contract.response.headers
    }

    def "a contract not matching method should return null"() {
        given:
            Contract contract = ContractTestFixtures.defaultGetContract().build()
            methodMatcher.findMatches(_ as Set<Contract>, GET) >> {EMPTY_SET}
            pathMatchingService.findMatches(_ as Set<Contract>,_ as String) >> {[contract]}
            headerMatcher.isMatch(_ as Set<Contract>,_ as Map<String, String>) >> {[contract]}
            bodyMatchService.findMatches(_ as Set<Contract>,_ as ContractRequest) >> {[contract]}
        when:
            Contract matchedContract = service.matchPostRequest(contract.request)
        then:
            matchedContract == null
    }

    def "a contract not matching the path should return null"() {
        given:
            Contract contract = ContractTestFixtures.defaultGetContract().build()
            methodMatcher.findMatches(_ as Set<Contract>, GET) >> {[contract]}
            pathMatchingService.findMatches(_ as Set<Contract>,_ as String) >> {EMPTY_SET}
            headerMatcher.isMatch(_ as Set<Contract>,_ as Map<String, String>) >> {[contract]}
            bodyMatchService.findMatches(_ as Set<Contract>,_ as ContractRequest) >> {[contract]}
        when:
            Contract matchedContract = service.matchPutRequest(contract.request)
        then:
            matchedContract == null
    }

    def "a contract not matching headers should return null"() {
        given:
            Contract contract = ContractTestFixtures.defaultGetContract().build()
            methodMatcher.findMatches(_ as Set<Contract>, GET) >> {[contract]}
            pathMatchingService.findMatches(_ as Set<Contract>,_ as String) >> {[contract]}
            headerMatcher.isMatch(_ as Set<Contract>,_ as Map<String, String>) >> {EMPTY_SET}
        when:
            Contract matchedContract = service.matchDeleteRequest(contract.request)
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
        when:
            service.matchGetRequest(contract1.request)
        then:
            thrown IllegalStateException
    }
}
