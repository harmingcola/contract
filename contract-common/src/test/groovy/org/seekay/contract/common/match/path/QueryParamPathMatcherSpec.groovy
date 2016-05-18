package org.seekay.contract.common.match.path

import spock.lang.Shared
import spock.lang.Specification

class QueryParamPathMatcherSpec extends Specification {

    @Shared QueryParamPathMatcher matcher = new QueryParamPathMatcher()

    def "a contract path with params and an actual path with none should not match" () {
        given:
            String contractPath = "/index?page=hello"
            String actualPath = "/index"
        when:
            boolean isMatch = matcher.isMatch(contractPath, actualPath)
        then:
            !isMatch
    }

    def "a contract path without params and an actual path with should not match" () {
        given:
            String contractPath = "/index"
            String actualPath = "/index?page=hello"
        when:
            boolean isMatch = matcher.isMatch(contractPath, actualPath)
        then:
            isMatch
    }

    def "two paths with the same params in different order should match" () {
        given:
            String contractPath = "/index?entry=allowed&page=hello"
            String actualPath = "/index?page=hello&entry=allowed"
        when:
            boolean isMatch = matcher.isMatch(contractPath, actualPath)
        then:
            isMatch
    }

    def "two paths with the same params in same order should match" () {
        given:
            String contractPath = "/index?&page=hello&entry=allowed"
            String actualPath = "/index?page=hello&entry=allowed"
        when:
            boolean isMatch = matcher.isMatch(contractPath, actualPath)
        then:
            isMatch
    }

    def "two paths with the same params with different values should not match" () {
        given:
            String contractPath = "/index?&page=hello&entry=allowed"
            String actualPath = "/index?page=hello&entry=denied"
        when:
            boolean isMatch = matcher.isMatch(contractPath, actualPath)
        then:
            !isMatch
    }

    def "two paths without params should match" () {
        given:
            String contractPath = "/index"
            String actualPath = "/index"
        when:
            boolean isMatch = matcher.isMatch(contractPath, actualPath)
        then:
            isMatch
    }
}
