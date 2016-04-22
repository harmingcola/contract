package org.seekay.contract.common
import com.fasterxml.jackson.databind.ObjectMapper
import org.seekay.contract.common.assertion.AssertionService
import org.seekay.contract.common.builder.ContractBuilder
import org.seekay.contract.common.enrich.EnricherService
import org.seekay.contract.common.match.MatchingService
import org.seekay.contract.common.match.body.BodyMatchService
import org.seekay.contract.common.matchers.ExactPathMatcher
import org.seekay.contract.common.matchers.HeaderMatcher
import org.seekay.contract.common.matchers.MethodMatcher
import org.seekay.contract.common.service.ContractService
import spock.lang.Specification

class ApplicationContextSpec extends Specification {

    def "a single contract service should be created" () {
        when:
            ContractService contractService1 = ApplicationContext.contractService()
        then:
            contractService1 != null
            contractService1 == ApplicationContext.contractService()
    }

    def "a single object mapper should be created" () {
        when:
            ObjectMapper objectMapper1 = ApplicationContext.objectMapper()
        then:
            objectMapper1 != null
            objectMapper1 == ApplicationContext.objectMapper()
    }

    def "a single contract builder should be created" () {
        when:
            ContractBuilder contractBuilder = ApplicationContext.contractBuilder()
        then:
            contractBuilder != null
            contractBuilder.objectMapper != null
            contractBuilder == ApplicationContext.contractBuilder()
    }

    def "a single matching service should be created" () {
        when:
            MatchingService matchingService = ApplicationContext.matchingService()
        then:
            matchingService != null
            matchingService.contractService != null
            matchingService.methodMatcher != null
            matchingService.exactPathMatcher != null
            matchingService.headerMatcher != null
            matchingService.bodyMatchService != null
            matchingService.objectMapper != null
            matchingService == ApplicationContext.matchingService()
    }

    def "a single exact path matcher should be created" () {
        when:
            ExactPathMatcher exactPathMatcher = ApplicationContext.exactPathMatcher()
        then:
            exactPathMatcher != null
            exactPathMatcher == ApplicationContext.exactPathMatcher()
    }

    def "a single method matcher should be created" () {
        when:
            MethodMatcher methodMatcher = ApplicationContext.methodMatcher()
        then:
            methodMatcher != null
            methodMatcher == ApplicationContext.methodMatcher()
    }

    def "a single header matcher should be created" () {
        when:
            HeaderMatcher headerMatcher = ApplicationContext.headerMatcher()
        then:
            headerMatcher != null
            headerMatcher == ApplicationContext.headerMatcher()
    }

    def "a body matching service should be created" () {
        when:
            BodyMatchService bodyMatchService = ApplicationContext.bodyMatchService()
        then:
            bodyMatchService != null
            bodyMatchService == ApplicationContext.bodyMatchService()
            !bodyMatchService.bodyMatchers.isEmpty()
    }

    def "an enricher service should be created" () {
        when:
            EnricherService enricherService = ApplicationContext.enricherService()
        then:
            enricherService != null
            enricherService == ApplicationContext.enricherService()
            !enricherService.enrichers.isEmpty()
    }

    def "an assertion service should be created" () {
        when:
            AssertionService assertionService = ApplicationContext.assertionService()
        then:
            assertionService != null
            assertionService == ApplicationContext.assertionService()
            !assertionService.asserters.isEmpty()
    }

    def "a context can be cleared and all singletons will be nulled" () {
        given:
            ApplicationContext.contractService()
            ApplicationContext.objectMapper()
            ApplicationContext.contractBuilder()
            ApplicationContext.matchingService()
            ApplicationContext.exactPathMatcher()
            ApplicationContext.methodMatcher()
            ApplicationContext.headerMatcher()
            ApplicationContext.bodyMatchService()
            ApplicationContext.enricherService()
        when:
            ApplicationContext.clear()
        then:
            ApplicationContext.contractService == null
            ApplicationContext.objectMapper == null
            ApplicationContext.contractBuilder == null
            ApplicationContext.matchingService == null
            ApplicationContext.exactPathMatcher == null
            ApplicationContext.methodMatcher == null
            ApplicationContext.headerMatcher == null
            ApplicationContext.bodyMatchService == null
            ApplicationContext.enricherService == null
    }
}