package org.seekay.contract.common.enrich
import org.seekay.contract.common.enrich.enrichers.Enricher
import org.seekay.contract.model.ContractTestFixtures
import org.seekay.contract.model.domain.Contract
import spock.lang.Specification

class EnricherServiceSpec extends Specification{

    Enricher uglyEnricher = Mock(Enricher)
    Enricher prettyEnricher = Mock(Enricher)

    EnricherService service = new EnricherService(
            enrichers: [
                    uglyEnricher, prettyEnricher
            ] as LinkedHashSet
    )

    def 'Enriching a contract will forward the call to every enricher implementation' () {
        given:
            Contract contract = ContractTestFixtures
                    .defaultPostContract()
                    .responseBody("Hello World")
                    .requestBody("Hello World")
                    .build()
            1 * uglyEnricher.enrichResponseBody("Hello World") >> {return "Goodbye World"}
            1 * prettyEnricher.enrichResponseBody("Goodbye World") >> {return "Goodbye Cruel World"}
        when:
            service.enrichResponseBody(contract)
        then:
            contract.response.body == 'Goodbye Cruel World'
    }

    def 'an enrichBody call will be forwarded to every enricher implementation' () {
        when:
            String result = service.enrichResponseBody("Hello World")
        then:
            1 * uglyEnricher.enrichResponseBody("Hello World") >> {return "Goodbye World"}
            1 * prettyEnricher.enrichResponseBody("Goodbye World") >> {return "Goodbye Cruel World"}
            result == "Goodbye Cruel World"
    }

}
