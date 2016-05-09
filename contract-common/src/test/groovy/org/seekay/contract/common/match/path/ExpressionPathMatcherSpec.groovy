package org.seekay.contract.common.match.path

import org.seekay.contract.common.match.common.ExpressionMatcher
import spock.lang.Shared
import spock.lang.Specification

class ExpressionPathMatcherSpec extends Specification {

    @Shared ExpressionPathMatcher matcher = new ExpressionPathMatcher()

    def setupSpec() {
        matcher.expressionMatcher = new ExpressionMatcher()
    }

    def 'identical strings shouldnt match, not this classes responsibility' () {
        expect:
            !matcher.isMatch('hello', 'hello')
    }

    def 'non identical strings not containing an expression should not match' () {
        expect:
            !matcher.isMatch('barry', 'ann')
    }
    
    def 'a string with nothing but an anyString expression should match a string' () {
        expect:
            matcher.isMatch('${contract.anyString}', 'hail hydra')
    }

    def 'a string with nothing but an anyString expression should not match an empty string' () {
        expect:
            matcher.isMatch('${contract.anyString}', '')
    }

    def 'a string containing an expression and other text should match a valid string' () {
        expect:
            matcher.isMatch('I am ${contract.anyString}', 'I am Iron Man')
    }

    def 'a nonsense expression will be ignored' () {
        expect:
            !matcher.isMatch('/${contract.gibberish}', 'I am Iron Man')
    }
}
