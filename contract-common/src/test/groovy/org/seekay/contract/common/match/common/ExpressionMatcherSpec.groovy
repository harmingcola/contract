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

    def 'a number variable expression should match' () {
        expect:
            matcher.isMatch('/index/${contract.var.number.repoId}', '/index/45')
            matcher.isMatch('/index/${contract.var.number.gibberish}', '/index/106')
    }

    def 'a positive number expression should match' () {
        expect:
            matcher.isMatch('/index/${contract.var.positiveNumber.repoId}', '/index/45')
            matcher.isMatch('/index/${contract.var.positiveNumber.gibberish}', '/index/106')
            !matcher.isMatch('/index/${contract.var.positiveNumber.gibberish}', '/index/-55')
    }

    def 'a string variable expression should match' () {
        expect:
            matcher.isMatch('/index/${contract.var.string.name}', '/index/bob')
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

    def 'a number alone should match the anyNumber expression' () {
        expect:
            matcher.isMatch('${contract.anyNumber}', "45")
    }

    def 'a negative floating point number alone should match the anyNumber expression' () {
        expect:
            matcher.isMatch('${contract.anyNumber}', "-23.47")
    }

    def 'a number with some text should match' () {
        expect:
            matcher.isMatch('I can count to ${contract.anyNumber}', "I can count to 89")

    }

    def 'all expressions in one string should match, ignoring timestamp'() {
        given:
            String contract = '${contract.anyString},${contract.anyNumber}'
            String actual = 'boop,-417.00'
        expect:
            matcher.isMatch(contract, actual)
    }

    def 'regex characters should be escaped' () {
        given:
            String contract = '''{"value" : "${contract.anyNumber}" }'''
            String actual = '''{"value" : 24 }'''
        expect:
            !matcher.isMatch(contract, actual)
    }
}
