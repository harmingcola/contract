package org.seekay.contract.model.tools
import spock.lang.Specification

import static org.seekay.contract.model.ContractTestFixtures.defaultGetContract

class ContractToolsSpec extends Specification {

    def "set tools should never be constructed" () {
        when:
        	SetTools.class.newInstance()
        then:
        	IllegalStateException e = thrown()
        	e.message == "Utility classes should never be constructed"
    }

    def "contracts should be filtered and only retain specified tags" () {
        given:
            def contracts = [
                defaultGetContract().tags("one", "delete").build(),
                defaultGetContract().tags("two", "delete").build(),
                defaultGetContract().tags("three", "delete").build()
            ]
        when:
            contracts = ContractTools.onlyIncludeTags(contracts, "three")
        then:
            contracts.size() == 1
    }

    def "a server should not run contracts with certain tags" () {
        given:
            def contracts = [
                defaultGetContract().tags("one", "delete").build(),
                defaultGetContract().tags("two", "delete").build(),
                defaultGetContract().tags("three", "delete").build()
            ]
        when:
            contracts = ContractTools.excludeTags(contracts, "two")
        then:
            contracts.size() == 2
    }

    def "a server should be able to both include and exclude features with one call" () {
        given:
            def contracts = [
                defaultGetContract().tags("one", "delete").build(),
                defaultGetContract().tags("two", "delete").build(),
                defaultGetContract().tags("three", "delete").build(),
                defaultGetContract().tags("four", "get").build()
            ]
        when:
            contracts = ContractTools.tags(contracts, ["delete"] as Set, ["one", "three"] as Set)
        then:
            contracts.size() == 1
    }
}
