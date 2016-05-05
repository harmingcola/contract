package org.seekay.contract.common.match.common

import spock.lang.Shared
import spock.lang.Specification

class ExpressionMatcherSpec extends Specification {

    @Shared ExpressionMatcher matcher = new ExpressionMatcher()

    def 'a contract string with no expression should not match' () {
        expect:
            !matcher.isMatch('/index', '/index')
    }

    def 'a contract string with a single expression should match' () {
        expect:
            matcher.isMatch('/${contract.anyString}', '/index')
    }

    def 'a contract string multiple expressions should match' () {
        expect:
            matcher.isMatch(
                   'Hi, my name is ${contract.anyString} I like ${contract.anyString} and ${contract.anyString}',
                   'Hi, my name is bob I like pizza and movies'
           )
    }

    def 'a timestamp in a string should match' () {
        given:
            String timestamp = String.valueOf(new Date().getTime())
        expect:
            matcher.isMatch(
                    'the current time is ${contract.timestamp}',
                    "the current time is $timestamp"
            )
    }

    def 'an incorrect expression throw an exception' () {
        when:
            !matcher.isMatch('/${contract.gibberish}', '/index')
        then:
            def e = thrown(IllegalStateException)
            e.message == 'Problem occurred compiling regex for : /${contract.gibberish}'
    }
}
