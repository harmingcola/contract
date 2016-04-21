package org.seekay.contract.common.match.body

import org.seekay.contract.model.ContractTestBuilder
import org.seekay.contract.model.domain.Contract
import spock.lang.Specification

class WhiteSpaceIgnoringBodyMatcherSpec extends Specification {

	WhiteSpaceIgnoringBodyMatcher matcher = new WhiteSpaceIgnoringBodyMatcher()

	def "when a contract contains an exact body match it should be returned" () {
		given:
			Contract simpleContract = ContractTestBuilder.post().requestBody("hello world").build()
		when:
			Set results = matcher.findMatches([simpleContract] as Set, "hello world")
		then:
			results.collect { Contract result ->
				result.request.body == simpleContract.request.body
			}
	}

	def "when multiple contracts contain an exact body match they should be returned" () {
		given:
			Contract simpleContract1 = ContractTestBuilder.post().requestBody("hello world").build()
			Contract simpleContract2 = ContractTestBuilder.post().requestBody("hello world").build()
		when:
			Set results = matcher.findMatches([simpleContract1, simpleContract2] as Set, "hello world")
		then:
		results.collect { Contract result ->
			result.request.body == simpleContract1.request.body
			result.request.body == simpleContract2.request.body
		}
	}

	def "when no contracts match the exact body an empty set should be returned" () {
		given:
			Contract simpleContract1 = ContractTestBuilder.post().requestBody("hello world").build()
			Contract simpleContract2 = ContractTestBuilder.post().requestBody("hello world").build()
		when:
			Set results = matcher.findMatches([simpleContract1, simpleContract2] as Set, "goodbye world")
		then:
			results.size() == 0
	}

	def "when a contract doesnt specify a body, and one has been received, it should not match" () {
		given:
			Contract contractWithNoBody = ContractTestBuilder.get().build()
		when:
			Set results = matcher.findMatches([contractWithNoBody] as Set, "goodbye world")
		then:
			results.size() == 0
	}

	def "when a contract doesnt specify a body, and none is received, it should match" () {
		given:
			Contract contractWithNoBody = ContractTestBuilder.get().build()
		when:
			Set results = matcher.findMatches([contractWithNoBody] as Set, null)
		then:
			results.size() == 1
	}
}
