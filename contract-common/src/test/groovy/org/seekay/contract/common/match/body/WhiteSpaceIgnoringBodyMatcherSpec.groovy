package org.seekay.contract.common.match.body
import org.seekay.contract.model.ContractTestBuilder
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

	def "when bodies are not the same, they should not match" () {
		given:
			Contract simpleContract = ContractTestBuilder.post().requestBody("hello world").build()
		expect:
			!matcher.isMatch(simpleContract.request.body, "goodbye world")
	}
}
