package org.seekay.contract.common
import com.fasterxml.jackson.databind.ObjectMapper
import org.seekay.contract.common.assertion.AssertionService
import org.seekay.contract.common.assertion.TimestampAsserter
import org.seekay.contract.common.builder.ContractBuilder
import org.seekay.contract.common.enrich.EnricherService
import org.seekay.contract.common.enrich.enrichers.TimestampEnricher
import org.seekay.contract.common.match.MatchingService
import org.seekay.contract.common.match.body.BodyMatchService
import org.seekay.contract.common.match.body.JsonBodyMatcher
import org.seekay.contract.common.match.body.WhiteSpaceIgnoringBodyMatcher
import org.seekay.contract.common.matchers.ExactPathMatcher
import org.seekay.contract.common.matchers.HeaderMatcher
import org.seekay.contract.common.matchers.MethodMatcher
import org.seekay.contract.common.service.ContractService

class ApplicationContext {

    static ContractService contractService
    static ObjectMapper objectMapper
    static ContractBuilder contractBuilder
    static MatchingService matchingService
    static ExactPathMatcher exactPathMatcher
    static MethodMatcher methodMatcher
    static BodyMatchService bodyMatchService
    static HeaderMatcher headerMatcher
    static EnricherService enricherService
    static AssertionService assertionService

    public static void clear() {
        contractService = null
        objectMapper = null
        contractBuilder = null
        matchingService = null
        exactPathMatcher = null
        methodMatcher = null
        headerMatcher = null
        bodyMatchService = null
        enricherService = null
        assertionService = null
    }

    public static ContractService contractService() {
        if(contractService == null) {
            contractService = new ContractService()
        }
        return contractService
    }

    public static ObjectMapper objectMapper() {
        if(objectMapper == null) {
            objectMapper = new ObjectMapper()
        }
        return objectMapper
    }

    public static ContractBuilder contractBuilder() {
        if(contractBuilder == null) {
            contractBuilder = new ContractBuilder(
                    objectMapper: objectMapper()
            )
        }
        return contractBuilder
    }

    public static MatchingService matchingService() {
        if(matchingService == null) {
            matchingService = new MatchingService(
                contractService: contractService(),
                methodMatcher: methodMatcher(),
                exactPathMatcher: exactPathMatcher(),
                headerMatcher: headerMatcher(),
                bodyMatchService: bodyMatchService(),
                objectMapper: objectMapper()
            )
        }
        return matchingService
    }

    public static ExactPathMatcher exactPathMatcher() {
        if(exactPathMatcher == null) {
            exactPathMatcher = new ExactPathMatcher()
        }
        return exactPathMatcher
    }

    public static MethodMatcher methodMatcher() {
        if(methodMatcher == null) {
            methodMatcher = new MethodMatcher()
        }
        return methodMatcher
    }

    public static HeaderMatcher headerMatcher() {
        if(headerMatcher == null) {
            headerMatcher = new HeaderMatcher()
        }
        return headerMatcher
    }

    public static BodyMatchService bodyMatchService() {
        if(bodyMatchService == null) {
            bodyMatchService = new BodyMatchService(
                bodyMatchers : [
                    new WhiteSpaceIgnoringBodyMatcher(),
<<<<<<< HEAD
                    new JsonBodyMatcher(objectMapper: objectMapper())
=======
                    new JsonBodyMatcher()
>>>>>>> Partial commit of json body matching stuff
                ] as LinkedHashSet
            )
        }
        return bodyMatchService
    }

    public static EnricherService enricherService() {
        if(enricherService == null) {
            enricherService = new EnricherService(
                enrichers: [
                        new TimestampEnricher()
                ] as LinkedHashSet
            )
        }
        return enricherService
    }

    public static AssertionService assertionService() {
        if(assertionService == null) {
            assertionService = new AssertionService(
                    asserters: [
                            new TimestampAsserter()
                    ] as LinkedHashSet
            )
        }
        return assertionService
    }
}
