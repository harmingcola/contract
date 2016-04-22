package org.seekay.contract.common.enrich.enrichers

import spock.lang.Specification

class TimestampEnricherSpec extends Specification {

    TimestampEnricher enricher = new TimestampEnricher()

    def "A body containing a timestamp should have the timestamp replaced" () {
        given:
            String body = 'The current time is ${contract.timestamp}'
        when:
            String enrichedBody = enricher.enrichResponseBody(body)
            String timestamp = new Date().getTime()
            timestamp = timestamp.substring(0,10)
        then:
            enrichedBody.contains("The current time is ${timestamp}")
    }

    def "A body containing nothing but a timestamp should have the timestamp replaced" () {
        given:
            String body = '${contract.timestamp}'
        when:
            String enrichedBody = enricher.enrichResponseBody(body)
            String timestamp = new Date().getTime()
            timestamp = timestamp.substring(0,10)
        then:
            enrichedBody.contains(timestamp)
    }

    def "A body containing multiple timestamps should have them replaced" () {
        given:
            String body = 'At ${contract.timestamp} the time will be ${contract.timestamp}'
        when:
            String enrichedBody = enricher.enrichResponseBody(body)
            String timestamp = new Date().getTime()
            timestamp = timestamp.substring(0,10)
        then:
            enrichedBody.contains("At ${timestamp}")
            enrichedBody.contains("the time will be ${timestamp}")
    }

    def "a body with no timestamp should not be changed" () {
        given:
            String body = 'annyeong worldd'
        when:
            String enrichedBody = enricher.enrichResponseBody(body)
        then:
            enrichedBody == body
    }

    def "a null body should be returned without error" () {
        when:
            String response = enricher.enrichResponseBody(null)
        then:
            response == null
    }
}
