package org.seekay.contract.common.matchers

import org.seekay.contract.common.match.common.ExpressionMatcher
import org.seekay.contract.model.domain.Contract
import spock.lang.Specification

import static org.seekay.contract.model.tools.SetTools.head
import static org.seekay.contract.model.ContractTestFixtures.defaultGetContract
import static org.seekay.contract.model.ContractTestFixtures.oneDefaultContractOfEachMethodWithoutHeaders

class HeaderMatcherSpec extends Specification {

    ExpressionMatcher expressionMatcher = Mock(ExpressionMatcher)

    HeaderMatcher matcher = new HeaderMatcher(
            expressionMatcher: expressionMatcher
    )

    def "a single request should be matched on its headers" () {
        given:
            Set<Contract> contracts = [defaultGetContract().build()]
        when:
            Set result = matcher.isMatch(contracts, ["captain":"america"])
        then:
            result.size() == 1
            head(result).request.headers["captain"] == "america"
    }

    def "a contract without request headers will match every http request" () {
        given:
            Set contracts = oneDefaultContractOfEachMethodWithoutHeaders()
        when:
            Set result = matcher.isMatch(contracts, ["captain":"america"])
        then:
            result.size() == contracts.size()
    }

    def "matcher can match several contracts" () {
        given:
            Set contracts = [
                defaultGetContract().path("/entity/1").requestHeaders(["iron":"man"]).responseBody("not filtered").build(),
                defaultGetContract().path("/entity/2").requestHeaders(["iron":"man"]).responseBody("not filtered").build(),
                defaultGetContract().path("/entity/3").requestHeaders(["steve":"rogers"]).responseBody("filtered").build()
            ]
        when:
            Set result = matcher.isMatch(contracts, ["iron":"man"])
        then:
            expressionMatcher.containsAnExpression(_) >> {return false}
            result.size() == 2
            result.collect { contract ->
                assert contract.response.body == "not filtered"
            }
    }

    def 'the matcher will match on headers containing expressions' () {
        given:
            Set contracts = [
                defaultGetContract().path("/entity/1").requestHeaders(["iron":'${contract.anyString}']).responseBody("not filtered").build(),
            ]
        when:
            Set result = matcher.isMatch(contracts, ["iron":"man"])
        then:
            expressionMatcher.containsAnExpression(_) >> {return true}
            expressionMatcher.isMatch(_,_) >> {return true}
            result.size() == 1
    }

    def 'the matcher will not match when the expression matcher doesnt match' () {
        given:
            Set contracts = [
                defaultGetContract().path("/entity/1").requestHeaders(["iron":'${contract.anyString}']).responseBody("not filtered").build(),
            ]
        when:
            Set result = matcher.isMatch(contracts, ["iron":"man"])
        then:
            expressionMatcher.containsAnExpression(_) >> {return true}
            expressionMatcher.isMatch(_,_) >> {return false}
            result.size() == 0
    }
}
