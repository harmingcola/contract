package org.seekay.contract.common.match.path

import org.seekay.contract.common.match.common.ExpressionMatcher
import spock.lang.Shared
import spock.lang.Specification

class ExpressionQueryParamPathMatcherSpec extends Specification {

    @Shared ExpressionQueryParamPathMatcher matcher = new ExpressionQueryParamPathMatcher()

    def setupSpec() {
        matcher.expressionMatcher = new ExpressionMatcher()
    }

    def 'a contract path with a single expression parameter should match' () {
        given:
            String contractPath = '/index?page=${contract.anyString}'
            String actualPath = '/index?page=hello'
        when:
            boolean isMatch = matcher.isMatch(contractPath, actualPath)
        then:
            isMatch
    }

    def 'a contract path with multiple expressions parameters in the wrong order should match' () {
        given:
            String contractPath = '/index?result=${contract.anyString}&page=${contract.anyString}'
            String actualPath = '/index?page=hello&result=win'
        when:
            boolean isMatch = matcher.isMatch(contractPath, actualPath)
        then:
            isMatch
    }

    def 'a contract path with more expression parameters than the actual should not match' () {
        given:
            String contractPath = '/index?result=${contract.anyString}&page=${contract.anyString}'
            String actualPath = '/index?page=hello'
        when:
            boolean isMatch = matcher.isMatch(contractPath, actualPath)
        then:
            !isMatch
    }

    def 'a contract path with less expression parameters than the actual should match' () {
        given:
            String contractPath = '/index?result=${contract.anyString}'
            String actualPath = '/index?page=hello&result=win&'
        when:
            boolean isMatch = matcher.isMatch(contractPath, actualPath)
        then:
            isMatch
    }

    def 'a contract path with no parameters should not match' () {
        given:
            String contractPath = '/index'
            String actualPath = '/index'
        when:
            boolean isMatch = matcher.isMatch(contractPath, actualPath)
        then:
            !isMatch
    }

    def 'a contract path with an incorrect expression should not match' () {
        given:
            String contractPath = '/index?result=${contract.gibberish}'
            String actualPath = '/index?result=success'
        when:
            boolean isMatch = matcher.isMatch(contractPath, actualPath)
        then:
            !isMatch
    }


}
