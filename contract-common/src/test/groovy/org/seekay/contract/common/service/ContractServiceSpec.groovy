package org.seekay.contract.common.service

import org.seekay.contract.model.ContractTestFixtures
import org.seekay.contract.model.domain.Contract
import spock.lang.Shared
import spock.lang.Specification

class ContractServiceSpec extends Specification {

	@Shared ContractService contractService = new ContractService()

	def "a newly created service should contain no contracts" () {
		expect:
			contractService.read().size() == 0
	}

	def "when contracts are added they should be stored" () {
		given:
			Contract contract = ContractTestFixtures.defaultGetContract().build()
		when:
			contractService.create(contract)
		then:
			contractService.read().size() == 1
	}
}
