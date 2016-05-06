package org.seekay.contract.common

import com.fasterxml.jackson.databind.ObjectMapper
import org.seekay.contract.common.builder.ContractBuilder
import org.seekay.contract.common.enrich.EnricherService
import org.seekay.contract.common.match.MatchingService
import org.seekay.contract.common.match.body.BodyMatchingService
import org.seekay.contract.common.match.body.ExpressionBodyMatcher
import org.seekay.contract.common.match.body.JsonBodyMatcher
import org.seekay.contract.common.match.body.WhiteSpaceIgnoringBodyMatcher
import org.seekay.contract.common.match.common.ExpressionMatcher
import org.seekay.contract.common.match.path.*
import org.seekay.contract.common.matchers.HeaderMatcher
import org.seekay.contract.common.matchers.MethodMatcher
import org.seekay.contract.common.service.ContractService

class ApplicationContext {

    static ContractService contractService
    static ObjectMapper objectMapper
    static ContractBuilder contractBuilder
    static MatchingService matchingService
    static PathMatchingService pathMatchingService
    static MethodMatcher methodMatcher
    static BodyMatchingService bodyMatchService
    static HeaderMatcher headerMatcher
    static EnricherService enricherService
    static ExpressionMatcher expressionMatcher

    public static void clear() {
        contractService = null
        objectMapper = null
        contractBuilder = null
        matchingService = null
        pathMatchingService = null
        methodMatcher = null
        headerMatcher = null
        bodyMatchService = null
        enricherService = null
        expressionMatcher = null
    }

    public static ContractService contractService() {
        if (contractService == null) {
            contractService = new ContractService()
        }
        return contractService
    }

    public static ObjectMapper objectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper()
        }
        return objectMapper
    }

    public static ContractBuilder contractBuilder() {
        if (contractBuilder == null) {
            contractBuilder = new ContractBuilder(
                    objectMapper: objectMapper()
            )
        }
        return contractBuilder
    }

    public static MatchingService matchingService() {
        if (matchingService == null) {
            matchingService = new MatchingService(
                    contractService: contractService(),
                    methodMatcher: methodMatcher(),
                    pathMatchingService: pathMatchingService(),
                    headerMatcher: headerMatcher(),
                    bodyMatchingService: bodyMatchingService(),
                    objectMapper: objectMapper()
            )
        }
        return matchingService
    }

    public static PathMatchingService pathMatchingService() {
        if (pathMatchingService == null) {
            pathMatchingService = new PathMatchingService(
                    exactPathMatcher: new ExactPathMatcher(),
                    queryParamPathMatcher: new QueryParamPathMatcher(),
                    expressionQueryParamPathMatcher: new ExpressionQueryParamPathMatcher(
                            expressionMatcher: expressionMatcher()
                    ),
                    expressionPathMatcher: new ExpressionPathMatcher(
                            expressionMatcher: expressionMatcher()
                    )
            )
        }
        return pathMatchingService
    }

    public static MethodMatcher methodMatcher() {
        if (methodMatcher == null) {
            methodMatcher = new MethodMatcher()
        }
        return methodMatcher
    }

    public static HeaderMatcher headerMatcher() {
        if (headerMatcher == null) {
            headerMatcher = new HeaderMatcher(
                    expressionMatcher: expressionMatcher()
            )
        }
        return headerMatcher
    }

    public static BodyMatchingService bodyMatchingService() {
        if (bodyMatchService == null) {
            bodyMatchService = new BodyMatchingService(
                    whiteSpaceIgnoringBodyMatcher: new WhiteSpaceIgnoringBodyMatcher(),
                    expressionBodyMatcher: new ExpressionBodyMatcher(
                            expressionMatcher: expressionMatcher()
                    ),
                    jsonBodyMatcher : new JsonBodyMatcher(
                            objectMapper: objectMapper(),
                            expressionMatcher: expressionMatcher()
                    )
            )
        }
        return bodyMatchService
    }

    public static EnricherService enricherService() {
        if (enricherService == null) {
            enricherService = new EnricherService()
        }
        return enricherService
    }

    public static ExpressionMatcher expressionMatcher() {
        if (expressionMatcher == null) {
            expressionMatcher = new ExpressionMatcher()
        }
        return expressionMatcher
    }
}
