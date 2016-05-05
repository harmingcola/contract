package org.seekay.contract.common.enrich

import org.seekay.contract.model.ContractTestFixtures
import org.seekay.contract.model.domain.Contract
import spock.lang.Shared
import spock.lang.Specification

class EnricherServiceSpec extends Specification {

    @Shared EnricherService service = new EnricherService()

    def 'a request path containing a timestamp should be enriched correctly ' () {
        given:
            Contract contract = ContractTestFixtures.defaultGetContract().path('/time/${contract.timestamp}').build()
        when:
            service.enrichRequest(contract)
            String timestamp = String.valueOf(new Date().getTime()).substring(0,9)
        then:
            contract.request.path.startsWith("/time/$timestamp")
    }

    def 'a request path containing an anyString should be enriched correctly ' () {
        given:
            Contract contract = ContractTestFixtures.defaultGetContract().path('/home/${contract.anyString}').build()
        when:
            service.enrichRequest(contract)
        then:
            contract.request.path.matches("/home/.*")
    }

    def 'a response body should be enriched correctly' () {
        given:
            Contract contract = ContractTestFixtures.defaultGetContract()
                    .responseBody('name,${contract.anyString},time,${contract.timestamp}').build()
        when:
            service.enrichResponse(contract)
            String[] chunks = contract.response.body.split(",")
            String timestamp = String.valueOf(new Date().getTime()).substring(0,9)
        then:
            chunks[1].matches(".*")
            chunks[3].startsWith(timestamp)
    }

    def 'a null string wont be enriched' () {
        when:
            def result = service.enrichString(null)
        then:
            result == null
            noExceptionThrown()
    }

    def 'an empty string wont be enriched' () {
        when:
            def result = service.enrichString('')
        then:
            result.isEmpty()
            noExceptionThrown()
    }
}
