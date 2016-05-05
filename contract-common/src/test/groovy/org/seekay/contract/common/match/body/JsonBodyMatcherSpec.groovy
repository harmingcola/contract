package org.seekay.contract.common.match.body
import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Shared
import spock.lang.Specification

import static org.seekay.contract.model.ContractTestFixtures.*

class JsonBodyMatcherSpec extends Specification {

    @Shared ObjectMapper objectMapper = new ObjectMapper()
    @Shared JsonBodyMatcher matcher = new JsonBodyMatcher()

    def setupSpec() {
        matcher.objectMapper = objectMapper
    }

    def "a json body should match an identical json body" () {
        given:
            String contractBody = objectMapper.writeValueAsString(defaultGetContract().build())
            String actualBody = objectMapper.writeValueAsString(defaultGetContract().build())
        expect:
            matcher.isMatch(contractBody, actualBody)
    }

    def "different json bodies should not match" () {
        given:
            String contractBody = objectMapper.writeValueAsString(defaultGetContract().build())
            String actualBody = objectMapper.writeValueAsString(defaultPostContract().build())
        expect:
            !matcher.isMatch(contractBody, actualBody)
    }

    def "json lists in a different order should match" () {
        given:
            def sortedContracts = (oneDefaultContractOfEachMethod() as List).sort()
            def unsortedContracts = oneDefaultContractOfEachMethod()
            String contractBody = objectMapper.writeValueAsString(sortedContracts)
            String actualBody = objectMapper.writeValueAsString(unsortedContracts)
        expect:
            matcher.isMatch(contractBody, actualBody)
    }

    def 'a json body that is a subset of another should not match' () {
        given:
            def contract = ["one":"two"]
            def actual = ["one":"two", "three":"four"]
            String contractBody = objectMapper.writeValueAsString(contract)
            String actualBody = objectMapper.writeValueAsString(actual)
        expect:
            !matcher.isMatch(contractBody, actualBody)
    }

    def 'a json body where keys have the same name but different types should not match' () {
        given:
            def contract = ["one":"two"]
            def actual = ["one":false]
            String contractBody = objectMapper.writeValueAsString(contract)
            String actualBody = objectMapper.writeValueAsString(actual)
        expect:
            !matcher.isMatch(contractBody, actualBody)
    }

    def 'a json body with different numbers should not match' () {
        given:
            def contract = ["one":1]
            def actual = ["one":2]
            String contractBody = objectMapper.writeValueAsString(contract)
            String actualBody = objectMapper.writeValueAsString(actual)
        expect:
            !matcher.isMatch(contractBody, actualBody)
    }

    def 'a json body with different strings should not match' () {
        given:
            def contract = ["one":"one"]
            def actual = ["one":"two"]
            String contractBody = objectMapper.writeValueAsString(contract)
            String actualBody = objectMapper.writeValueAsString(actual)
        expect:
            !matcher.isMatch(contractBody, actualBody)
    }

    def 'a json body with different booleans should not match' () {
        given:
            def contract = ["one":true]
            def actual = ["one":false]
            String contractBody = objectMapper.writeValueAsString(contract)
            String actualBody = objectMapper.writeValueAsString(actual)
        expect:
            !matcher.isMatch(contractBody, actualBody)
    }

    def 'a json body with a list of different objects should not match' () {
        given:
            def contract = ["one":["two", "three"]]
            def actual = ["one":["four", "five"]]
            String contractBody = objectMapper.writeValueAsString(contract)
            String actualBody = objectMapper.writeValueAsString(actual)
        expect:
            !matcher.isMatch(contractBody, actualBody)
    }

    def 'a json body with a list objects in a different order should match' () {
        given:
            def contract = ["one":["two","three","four","five"]]
            def actual = ["one":["five","four","three","two"]]
            String contractBody = objectMapper.writeValueAsString(contract)
            String actualBody = objectMapper.writeValueAsString(actual)
        expect:
            matcher.isMatch(contractBody, actualBody)
    }

    def 'a json body with a list objects of different size shouldnt match' () {
        given:
            def contract = ["one":["two","three","four","five"]]
            def actual = ["one":["five","four","three"]]
            String contractBody = objectMapper.writeValueAsString(contract)
            String actualBody = objectMapper.writeValueAsString(actual)
        expect:
            !matcher.isMatch(contractBody, actualBody)
    }

    def 'an error thrown parsing json should be treated as not a match' () {
        given:
            ObjectMapper mockObjectMapper = Mock(ObjectMapper)
            JsonBodyMatcher matcher = new JsonBodyMatcher(objectMapper: mockObjectMapper)
            def contract = ["one":["two","three","four","five"]]
            def actual = ["one":["five","four","three"]]
            String contractBody = this.objectMapper.writeValueAsString(contract)
            String actualBody = this.objectMapper.writeValueAsString(actual)
            1 * mockObjectMapper.readValue(_, Object.class) >> {throw new IOException()}
        expect:
            !matcher.isMatch(contractBody, actualBody)
    }
}
