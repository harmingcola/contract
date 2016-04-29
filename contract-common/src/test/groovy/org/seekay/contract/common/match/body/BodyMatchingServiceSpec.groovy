package org.seekay.contract.common.match.body
import org.seekay.contract.model.ContractTestBuilder
import org.seekay.contract.model.domain.Contract
import spock.lang.Specification

import static org.seekay.contract.model.ContractTestFixtures.*

class BodyMatchingServiceSpec extends Specification {

    BodyMatcher uglyBodyMatcher = Mock(BodyMatcher)
    BodyMatcher prettyBodyMatcher = Mock(BodyMatcher)

    BodyMatchingService service = new BodyMatchingService(
            bodyMatchers: [
                    uglyBodyMatcher, prettyBodyMatcher
            ] as LinkedHashSet
    )

    def 'an isMatch call will be forwarded to every body matcher implementation and if found, return false'() {
        when:
            def matchFound = service.isMatch("hello world", "Hello World")
        then:
            1 * uglyBodyMatcher.isMatch(_ as String, _ as String) >> { return false }
            1 * prettyBodyMatcher.isMatch(_ as String, _ as String) >> { return false }
            !matchFound
    }

    def "when a contract doesnt specify a body, and one has been received, it should not match"() {
        given:
            Contract contractWithNoBody = ContractTestBuilder.get().build()
        expect:
            !service.isMatch(contractWithNoBody.request.body, "goodbye world")
    }

    def "when a contract doesnt specify a body, and none is received, it should match"() {
        given:
            Contract contractWithNoBody = ContractTestBuilder.get().build()
        expect:
            service.isMatch(contractWithNoBody.request.body, null)
    }

    def "when a contract specifies a response body, and none is received, it shouldnt match"() {
        given:
            Contract contract = defaultGetContract().build()
        expect:
            !service.isMatch(contract.response.body, null)
    }

    def "when the contact response is null, and the actual response is an empty string, they should match"() {
        given:
            Contract contractWithNullResponseBody = defaultDeleteContract().responseBody(null).build()
        expect:
            service.isMatch(contractWithNullResponseBody.request.body, "")
    }

    def "for two populated bodies, the matchers should be called"() {
        given:
            def contracts = [defaultPostContract().build()] as Set
        when:
            def matchFound = service.findMatches(contracts, defaultPostContract().build().request)
        then:
            1 * uglyBodyMatcher.isMatch(_ as String, _ as String) >> { return false }
            1 * prettyBodyMatcher.isMatch(_ as String, _ as String) >> { return true }
            matchFound
    }

    def "for invalid bodies, the matchers should not called"() {
        given:
            def contracts = [defaultPostContract().build()] as Set
        when:
            def matchFound = service.findMatches(contracts, defaultGetContract().requestBody(null).build().request)
        then:
            0 * uglyBodyMatcher.isMatch(_ as String, _ as String)
            0 * prettyBodyMatcher.isMatch(_ as String, _ as String)
            !matchFound
    }

}
