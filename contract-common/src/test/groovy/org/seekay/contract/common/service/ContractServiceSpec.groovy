package org.seekay.contract.common.service

import org.seekay.contract.model.domain.Contract
import spock.lang.Shared
import spock.lang.Specification

import static org.seekay.contract.model.ContractTestFixtures.defaultGetContract
import static org.seekay.contract.model.ContractTestFixtures.oneDefaultContractOfEachMethod

class ContractServiceSpec extends Specification {

	@Shared ContractService contractService = new ContractService()

	def "a newly created service should contain no contracts" () {
		expect:
			contractService.read().size() == 0
	}

	def "when contracts are added they should be stored" () {
		given:
			Contract contract = defaultGetContract().build()
		when:
			contractService.create(contract)
		then:
			contractService.read().size() == 1
	}

	def 'all contracts can be removed from the service' () {
		given:
			ContractService service = new ContractService()
			for(Contract contract : oneDefaultContractOfEachMethod()) {
				service.create(contract)
			}
			assert service.read().size() == 4
		when:
			service.deleteContracts()
		then:
			service.read().size() == 0
	}
}
