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

    def 'the end part of a for break should cause an expression not to match' () {
        given:ExpressionMatcherSpec
            String contract = '''/ct/services/servers/${contract.var.string.serverId}'''
            String actual = '''/ct/services/servers/4/start'''
        expect:
            !matcher.isMatch(contract, actual)
    }

    def 'A url should match a variable' () {
        given:
            String contract = '''${contract.var.string.repoUrl}'''
            String actual = '''https://github.com/harmingcola/kvServerContracts.git'''
        expect:
            matcher.isMatch(contract, actual)
    }

    def 'A url should match as anyString' () {
        given:
            String contract = '''${contract.anyString}'''
            String actual = '''https://github.com/harmingcola/kvServerContracts.git'''
        expect:
            matcher.isMatch(contract, actual)
    }

    def 'A positive number variable should match' () {
        given:
            String contract = '''${contract.var.positiveNumber.port}'''
            String actual = '''1744096943'''
        expect:
            matcher.isMatch(contract, actual)
    }
}
