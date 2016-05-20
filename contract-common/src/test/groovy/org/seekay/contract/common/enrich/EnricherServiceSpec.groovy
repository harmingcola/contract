package org.seekay.contract.common.enrich

import org.seekay.contract.common.variable.VariableStore
import org.seekay.contract.model.domain.Contract
import spock.lang.Specification

import static org.seekay.contract.model.ContractTestFixtures.defaultGetContract
import static org.seekay.contract.model.ContractTestFixtures.defaultPostContract

class EnricherServiceSpec extends Specification {

    EnricherService service = new EnricherService()

    VariableStore variableStore = Mock(VariableStore)

    def setup() {
        service.variableStore = variableStore
    }

    def 'a request path containing a timestamp should be enriched correctly ' () {
        given:
            Contract contract = defaultGetContract().path('/time/${contract.timestamp}').build()
        when:
            contract = service.enrichRequest(contract)
            String timestamp = String.valueOf(new Date().getTime()).substring(0,9)
        then:
            contract.request.path.startsWith("/time/$timestamp")
    }

    def 'a request path containing an anyString should be enriched correctly ' () {
        given:
            Contract contract = defaultGetContract().path('/home/${contract.anyString}').build()
        when:
            contract = service.enrichRequest(contract)
        then:
            contract.request.path.matches("/home/.*")
    }

    def 'a response body should be enriched correctly' () {
        given:
            Contract contract = defaultGetContract()
                    .responseBody('name,${contract.anyString},time,${contract.timestamp}').build()
        when:
            contract = service.enrichResponse(contract)
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

    def 'headers in a request should be enriched' () {
        given:
            Contract contract = defaultGetContract()
                    .requestHeaders([
                        'generatedAt':'${contract.timestamp}',
                        'cacheKey':'${contract.anyString}'
                    ]).build()
        when:
            contract = service.enrichRequest(contract)
            String timestamp = String.valueOf(new Date().getTime()).substring(0,9)
        then:
            contract.request.headers['generatedAt'].startsWith(timestamp)
            Dictionary.words.contains(contract.request.headers['cacheKey'])
    }

    def 'headers in a response should be enriched' () {
        given:
            Contract contract = defaultGetContract()
                    .responseHeaders([
                        'generatedAt':'${contract.timestamp}',
                        'cacheKey':'${contract.anyString}'
            ]).build()
        when:
            contract = service.enrichResponse(contract)
            String timestamp = String.valueOf(new Date().getTime()).substring(0,9)
        then:
            contract.response.headers['generatedAt'].startsWith(timestamp)
            Dictionary.words.contains(contract.response.headers['cacheKey'])
    }

    def 'multiple numbers will be replaced with different values' () {
        given:
            Contract contract = defaultPostContract()
                .requestBody('${contract.anyNumber},${contract.anyNumber},${contract.anyNumber}')
                .build()
        when:
            contract = service.enrichRequest(contract)
            String[] chunks = contract.request.body.split(",")
        then:
            chunks[0] != chunks[1]
            chunks[1] != chunks[2]
    }

    def 'multiple anyStrings will be replaced with different values' () {
        given:
            Contract contract = defaultPostContract()
                .requestBody('${contract.anyString},${contract.anyString},${contract.anyString}')
                .build()
        when:
            contract = service.enrichRequest(contract)
            String[] chunks = contract.request.body.split(",")
        then:
            chunks[0] != chunks[1]
            chunks[1] != chunks[2]
    }

    def 'a request path nothing but a variable should be enriched correctly ' () {
        given:
            Contract contract = defaultGetContract().path('/home/${contract.var.number.id}').build()
        when:
            contract = service.enrichRequest(contract)
        then:
            contract.request.path.matches('/home/45505')
            1 * variableStore.get('id') >> {return 45505}
    }

    def 'a request path containing a number variable should be enriched correctly ' () {
        given:
            Contract contract = defaultGetContract().path('/home/${contract.var.number.id}').build()
        when:
            contract = service.enrichRequest(contract)
        then:
            contract.request.path.matches('/home/45505')
            1 * variableStore.get('id') >> {return 45505}
    }

    def 'a request path containing a positive number variable should be enriched correctly ' () {
        given:
            Contract contract = defaultGetContract().path('/home/${contract.var.positiveNumber.id}').build()
        when:
            contract = service.enrichRequest(contract)
        then:
            contract.request.path.matches('/home/45505')
            1 * variableStore.get('id') >> {return 45505}
    }

    def 'a request path containing a multiple variables should be enriched correctly ' () {
        given:
            Contract contract = defaultGetContract().path('/home/${contract.var.number.eventid}/${contract.var.number.marketid}').build()
        when:
            contract = service.enrichRequest(contract)
        then:
            contract.request.path.matches('/home/45505/11011')
            1 * variableStore.get('eventid') >> {return 45505}
            1 * variableStore.get('marketid') >> {return 11011}
    }

    def 'a request path containing a string variable should be enriched correctly ' () {
        given:
            Contract contract = defaultGetContract().path('/home/${contract.var.string.name}').build()
        when:
            contract = service.enrichRequest(contract)
        then:
            contract.request.path.matches('/home/bobby')
            1 * variableStore.get('name') >> {return 'bobby'}
    }

    def 'if a number variable doesnt exist, one will be generated' () {
        given:
            Contract contract = defaultGetContract().path('${contract.var.number.eventid}').build()
        when:
            contract = service.enrichRequest(contract)
            String eventid = contract.request.path
        then:
            eventid != null
            1 * variableStore.get('eventid') >> {return null}
    }

    def 'if a positive number variable doesnt exist, one will be generated' () {
        given:
            Contract contract = defaultGetContract().path('${contract.var.positiveNumber.eventid}').build()
        when:
            contract = service.enrichRequest(contract)
            String eventid = contract.request.path
        then:
            eventid != null
            1 * variableStore.get('eventid') >> {return null}
    }

    def 'if a string variable doesnt exist, one will be generated' () {
        given:
            Contract contract = defaultGetContract().path('${contract.var.string.name}').build()
        when:
            contract = service.enrichRequest(contract)
            String name = contract.request.path
        then:
            name != null
            1 * variableStore.get('name') >> {return null}
    }
}
