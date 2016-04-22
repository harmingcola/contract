package org.seekay.contract.common.match.body

import org.seekay.contract.model.ContractTestBuilder
import org.seekay.contract.model.ContractTestFixtures
import org.seekay.contract.model.domain.Contract
import spock.lang.Specification

class WhiteSpaceIgnoringBodyMatcherSpec extends Specification {

	WhiteSpaceIgnoringBodyMatcher matcher = new WhiteSpaceIgnoringBodyMatcher()

	def "when a contract contains an exact body match it should be returned" () {
		given:
			Contract simpleContract = ContractTestBuilder.post().requestBody("hello world").build()
		expect:
			matcher.isMatch(simpleContract.request.body, "hello world")
	}

	def "when a contract doesnt specify a body, and one has been received, it should not match" () {
		given:
			Contract contractWithNoBody = ContractTestBuilder.get().build()
		expect:
			!matcher.isMatch(contractWithNoBody.request.body, "goodbye world")
	}

	def "when a contract doesnt specify a body, and none is received, it should match" () {
		given:
			Contract contractWithNoBody = ContractTestBuilder.get().build()
		expect:
			matcher.isMatch(contractWithNoBody.request.body, null)
	}

	def "when the contact response is null, and the actual response is an empty string, they should match" () {
		given:
			Contract contractWithNullResponseBody = ContractTestFixtures.defaultDeleteContract().responseBody(null).build()
		expect:
			matcher.isMatch(contractWithNullResponseBody.request.body, "")
	}

	def "when bodies are not the same, they should not match" () {
		given:
			Contract simpleContract = ContractTestBuilder.post().requestBody("hello world").build()
		expect:
			!matcher.isMatch(simpleContract.request.body, "goodbye world")
	}
}
