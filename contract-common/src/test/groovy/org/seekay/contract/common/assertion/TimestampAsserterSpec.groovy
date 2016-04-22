package org.seekay.contract.common.assertion

import org.seekay.contract.model.ContractTestFixtures
import org.seekay.contract.model.domain.Contract
import org.seekay.contract.model.domain.ContractResponse
import spock.lang.Specification

class TimestampAsserterSpec extends Specification {

    TimestampAsserter asserter = new TimestampAsserter()

    def 'a contract containing a wildcard timestamp and a response with an actual timestamp should throw no error and the contract should be updated' () {
        given:
            Contract contractWithWildcardTimestamp = ContractTestFixtures
					.defaultGetContract()
					.responseBody('${contract.timestamp}')
					.build()
            Contract contractWithCurrentTime = ContractTestFixtures
					.defaultGetContract()
                    .responseBody("${new Date().getTime()}")
					.build()
        when:
            asserter.assertOnWildCards(contractWithWildcardTimestamp.response, contractWithCurrentTime.response)
            String timestamp = new Date().getTime()
            timestamp = timestamp.substring(0,10)
        then:
            contractWithWildcardTimestamp.response.body.contains(timestamp)
    }

    def 'a contract containing a wildcard timestamp with other test and a response with an actual timestamp should throw no error and the contract should be updated' () {
        given:
            Contract contractWithWildcardTimestamp = ContractTestFixtures
					.defaultGetContract()
					.responseBody('The current time is : ${contract.timestamp}').build()
            Contract contractWithCurrentTime = ContractTestFixtures
					.defaultGetContract()
                	.responseBody("The current time is : ${new Date().getTime()}")
					.build()
        when:
            asserter.assertOnWildCards(contractWithWildcardTimestamp.response, contractWithCurrentTime.response)
            String timestamp = new Date().getTime()
            timestamp = timestamp.substring(0,10)
        then:
            contractWithWildcardTimestamp.response.body.contains("The current time is : ${timestamp}")
    }

    def 'a contract multiple wildcard timestamps with other test and a response with an actual timestamp should throw no error and the contract should be updated' () {
        given:
            Contract contractWithWildcardTimestamp = ContractTestFixtures
					.defaultGetContract()
                	.responseBody('At ${contract.timestamp} the time will be ${contract.timestamp}')
					.build()
            def sharedTimestamp = new Date().getTime()
            Contract contractWithCurrentTime = ContractTestFixtures
					.defaultGetContract()
                	.responseBody("At ${sharedTimestamp} the time will be ${sharedTimestamp}")
					.build()
        when:
            asserter.assertOnWildCards(contractWithWildcardTimestamp.response, contractWithCurrentTime.response)
            String timestamp = new Date().getTime()
            timestamp = timestamp.substring(0,10)
        then:
            contractWithWildcardTimestamp.response.body.contains("At ${timestamp}")
            contractWithWildcardTimestamp.response.body.contains("the time will be ${timestamp}")
    }

	def 'a response with no body should cause no errors' () {
		given:
			ContractResponse noBodyResponse = ContractTestFixtures
					.defaultDeleteContract()
					.responseBody(null)
					.build()
					.response
		when:
			asserter.assertOnWildCards(noBodyResponse, null)
		then:
			noExceptionThrown()
	}
}
