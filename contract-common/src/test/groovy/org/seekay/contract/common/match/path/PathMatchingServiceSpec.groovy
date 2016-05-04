package org.seekay.contract.common.match.path
import spock.lang.Specification

import static org.seekay.contract.model.ContractTestFixtures.defaultPostContract

class PathMatchingServiceSpec extends Specification {

    ExactPathMatcher exactPathMatcher = Mock(ExactPathMatcher)
    QueryParamPathMatcher queryParamPathMatcher = Mock(QueryParamPathMatcher)
    ExpressionPathMatcher expressionPathMatcher = Mock(ExpressionPathMatcher)

    PathMatchingService service = new PathMatchingService(
        exactPathMatcher: exactPathMatcher,
        queryParamPathMatcher: queryParamPathMatcher,
        expressionPathMatcher: expressionPathMatcher
    )

    def "for two paths, the matchers should be called, result found"() {
        given:
            def contracts = [defaultPostContract().path('/index').build()] as Set
        when:
            def matches = service.findMatches(contracts, "/index")
        then:
            1 * exactPathMatcher.isMatch(_ as String, _ as String) >> { return false }
            1 * queryParamPathMatcher.isMatch(_ as String, _ as String) >> { return false }
            1 * expressionPathMatcher.isMatch(_ as String, _ as String) >> { return true }
            matches.size() == 1
    }

    def "for two paths, the matchers should be called, no result found"() {
        given:
            def contracts = [defaultPostContract().path('/index').build()] as Set
        when:
            def matches = service.findMatches(contracts, "/index")
        then:
            1 * exactPathMatcher.isMatch(_ as String, _ as String) >> { return false }
            1 * queryParamPathMatcher.isMatch(_ as String, _ as String) >> { return false }
            1 * expressionPathMatcher.isMatch(_ as String, _ as String) >> { return false }
            matches.size() == 0
    }

    def 'if there is an exact match, the queryParam and expression matcher shouldnt be called' () {
        given:
            def contracts = [defaultPostContract().path('/index').build()] as Set
        when:
            def matches = service.findMatches(contracts, "/index")
        then:
            1 * exactPathMatcher.isMatch(_ as String, _ as String) >> { return true }
            0 * queryParamPathMatcher.isMatch(_ as String, _ as String)
            0 * expressionPathMatcher.isMatch(_ as String, _ as String)
            matches.size() == 1
    }

    def 'if there is no exact match, the query param matcher matches and expression doesnt match' () {
        given:
            def contracts = [defaultPostContract().path('/index').build()] as Set
        when:
            def matches = service.findMatches(contracts, "/index")
        then:
            1 * exactPathMatcher.isMatch(_ as String, _ as String) >> { return false }
            1 * queryParamPathMatcher.isMatch(_ as String, _ as String) >> {return true}
            0 * expressionPathMatcher.isMatch(_ as String, _ as String)
            matches.size() == 1
    }

    def 'two nulls should match' () {
        when:
            def isMatch = service.isMatch(exactPathMatcher, null, null);
        then:
            isMatch
    }

    def 'Contract null and actual not null should not match' () {
        when:
           def isMatch = service.isMatch(exactPathMatcher, null, "/index");
        then:
            !isMatch
    }

    def 'Contract not null and actual null should not match' () {
        when:
            def isMatch = service.isMatch(exactPathMatcher, "/index", null);
        then:
            !isMatch
    }
}
