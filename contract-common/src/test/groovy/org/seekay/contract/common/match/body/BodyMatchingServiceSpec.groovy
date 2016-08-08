package org.seekay.contract.common.match.body

import org.seekay.contract.common.ApplicationContext
import spock.lang.Specification

import static org.seekay.contract.common.ApplicationContext.*
import static org.seekay.contract.model.ContractTestFixtures.defaultGetContract
import static org.seekay.contract.model.ContractTestFixtures.defaultPostContract

class BodyMatchingServiceSpec extends Specification {

    WhiteSpaceIgnoringBodyMatcher whiteSpaceIgnoringBodyMatcher = Mock(WhiteSpaceIgnoringBodyMatcher)
    ExpressionBodyMatcher expressionBodyMatcher = Mock(ExpressionBodyMatcher)
    JsonBodyMatcher jsonBodyMatcher = Mock(JsonBodyMatcher)
    SymmetricJsonBodyMatcher symmetricJsonBodyMatcher = Mock(SymmetricJsonBodyMatcher)

    BodyMatchingService service = new BodyMatchingService(
            whiteSpaceIgnoringBodyMatcher: whiteSpaceIgnoringBodyMatcher,
            expressionBodyMatcher: expressionBodyMatcher,
            jsonBodyMatcher :jsonBodyMatcher,
            symmetricJsonBodyMatcher: symmetricJsonBodyMatcher
    )

    def 'if no exact body match is found, the expression body matcher should be called'() {
        given:
            def contracts = [defaultPostContract().build()] as Set
        when:
            def matches = service.findMatches(contracts, defaultPostContract().build().request)
        then:
            1 * whiteSpaceIgnoringBodyMatcher.isMatch(_ as String, _ as String) >> { return false }
            1 * expressionBodyMatcher.isMatch(_ as String, _ as String) >> { return true }
            0 * jsonBodyMatcher.isMatch(_ as String, _ as String)
            matches.size() == 1
    }

    def 'if an exact body match is found, no other matcher is checked' () {
        given:
            def contracts = [defaultPostContract().build()] as Set
        when:
            def matches = service.findMatches(contracts, defaultPostContract().build().request)
        then:
            1 * whiteSpaceIgnoringBodyMatcher.isMatch(_ as String, _ as String) >> { return true }
            0 * expressionBodyMatcher.isMatch(_ as String, _ as String)
            0 * jsonBodyMatcher.isMatch(_ as String, _ as String)
            matches.size() == 1
    }

    def 'if an expression body match is found, no other matcher is checked' () {
        given:
            def contracts = [defaultPostContract().build()] as Set
        when:
            def matches = service.findMatches(contracts, defaultPostContract().build().request)
        then:
            1 * whiteSpaceIgnoringBodyMatcher.isMatch(_ as String, _ as String) >> { return false }
            1 * expressionBodyMatcher.isMatch(_ as String, _ as String) >> { return true }
            0 * jsonBodyMatcher.isMatch(_ as String, _ as String)
            matches.size() == 1
    }

    def 'if a json body match is found, the result should be true' () {
        given:
            def contracts = [defaultPostContract().build()] as Set
        when:
            def matches = service.findMatches(contracts, defaultPostContract().build().request)
        then:
            1 * whiteSpaceIgnoringBodyMatcher.isMatch(_ as String, _ as String) >> { return false }
            1 * expressionBodyMatcher.isMatch(_ as String, _ as String) >> { return false }
            1 * jsonBodyMatcher.isMatch(_ as String, _ as String) >> { return true }
            matches.size() == 1
    }

    def 'for invalid bodies, the matchers should not called'() {
        given:
            def contracts = [defaultPostContract().build()] as Set
        when:
            def matches = service.findMatches(contracts, defaultGetContract().requestBody(null).build().request)
        then:
            0 * whiteSpaceIgnoringBodyMatcher.isMatch(_ as String, _ as String)
            0 * expressionBodyMatcher.isMatch(_ as String, _ as String)
            0 * jsonBodyMatcher.isMatch(_ as String, _ as String)
            matches.size() == 0
    }

    def 'a contract and an empty body should not match without calling matchers' () {
        given:
            def contracts = [defaultPostContract().requestBody("expected text").build()] as Set
        when:
            def matches = service.findMatches(contracts, defaultGetContract().requestBody(" ").build().request)
        then:
            0 * whiteSpaceIgnoringBodyMatcher.isMatch(_ as String, _ as String)
            0 * jsonBodyMatcher.isMatch(_ as String, _ as String)
            matches.size() == 0
    }

    def 'for two bodies, if a whitespace match is found, no other matcher is called'() {
        when:
            def isMatch = service.isMatch('/index', '/index')
        then:
            1 * whiteSpaceIgnoringBodyMatcher.isMatch(_ as String, _ as String) >> { return true }
            0 * jsonBodyMatcher.isMatch(_ as String, _ as String)
            isMatch
    }

    def 'for two bodies, if no whitespace match is found, the Json Body Matcher is called'() {
        when:
            def isMatch = service.isMatch('/index', '/index')
        then:
            1 * whiteSpaceIgnoringBodyMatcher.isMatch(_ as String, _ as String) >> { return false }
            1 * jsonBodyMatcher.isMatch(_ as String, _ as String) >> { return true }
            isMatch
    }

    def 'for two bodies, if no matcher matches, there is no match'() {
        when:
            def isMatch = service.isMatch('/index', '/index')
        then:
            1 * whiteSpaceIgnoringBodyMatcher.isMatch(_ as String, _ as String) >> { return false }
            1 * jsonBodyMatcher.isMatch(_ as String, _ as String) >> { return false }
        !isMatch
    }

    def 'if both the contract and actual bodies are null, they match' () {
        expect:
            !service.isMatch(jsonBodyMatcher, null, null)
    }

    def 'two null bodies should match' () {
        expect:
            service.isMatch(null, null)
    }

    def 'two empty string bodies should match' () {
        expect:
            service.isMatch("", "")
    }

    def 'a null contract body and an empty response should match' () {
        expect:
            service.isMatch(null, "")
    }

    def 'Integration test 001' () {
        given:
            BodyMatchingService bodyMatchingService = bodyMatchingService()
            String contractBody = '{"id":"S001","name":"${contract.var.string.name}","repositoryId":"R001","port":"${contract.var.positiveNumber.port}","status":"STARTED"}'
            String actualBody = '{"id":"S001","name":"hair","repositoryId":"R001","port":1744096943,"status":"STARTED"}'
        expect:
            bodyMatchingService.isMatch(contractBody, actualBody)
    }
}
