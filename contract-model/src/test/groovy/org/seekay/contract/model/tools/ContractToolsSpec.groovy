package org.seekay.contract.model.tools

import spock.lang.Specification

import static org.seekay.contract.model.ContractTestFixtures.defaultGetContract
import static org.seekay.contract.model.tools.ContractTools.*

class ContractToolsSpec extends Specification {

    def "ContractTools should never be constructed" () {
        when:
            ContractTools.class.newInstance()
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
            retainTags(contracts, "three")
        then:
            enabledCount(contracts) == 1
    }

    def "a server should not run contracts with certain tags" () {
        given:
            def contracts = [
                defaultGetContract().tags("one", "delete").build(),
                defaultGetContract().tags("two", "delete").build(),
                defaultGetContract().tags("three", "delete").build()
            ]
        when:
            excludeTags(contracts, "two")
        then:
            enabledCount(contracts) == 2
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
            tags(contracts, ["delete"] as Set, ["one", "three"] as Set)
        then:
            enabledCount(contracts) == 1
    }


}
