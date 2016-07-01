package org.seekay.contract.common

import com.fasterxml.jackson.databind.ObjectMapper
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
import org.seekay.contract.common.variable.JsonBodyVariableExtractor
import org.seekay.contract.common.variable.StringVariableExtractor
import org.seekay.contract.common.variable.VariableStore
import org.seekay.contract.configuration.LocalConfigurationSource

import static com.fasterxml.jackson.annotation.JsonInclude.Include
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES

class ApplicationContext {

    static ContractService contractService
    static ObjectMapper objectMapper
    static MatchingService matchingService
    static PathMatchingService pathMatchingService
    static MethodMatcher methodMatcher
    static BodyMatchingService bodyMatchService
    static HeaderMatcher headerMatcher
    static EnricherService enricherService
    static ExpressionMatcher expressionMatcher
    static LocalConfigurationSource localConfigurationSource
    static VariableStore variableStore
    static JsonBodyMatcher jsonBodyMatcher
    static JsonBodyVariableExtractor jsonBodyVariableExtractor

    public static void clear() {
        contractService = null
        objectMapper = null
        matchingService = null
        pathMatchingService = null
        methodMatcher = null
        headerMatcher = null
        bodyMatchService = null
        enricherService = null
        expressionMatcher = null
        localConfigurationSource = null
        variableStore = null
        jsonBodyMatcher = null
        jsonBodyVariableExtractor = null
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
            objectMapper.setSerializationInclusion(Include.NON_NULL)
            objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
        return objectMapper
    }

    public static MatchingService matchingService() {
        if (matchingService == null) {
            matchingService = new MatchingService(
                    contractService: contractService(),
                    methodMatcher: methodMatcher(),
                    pathMatchingService: pathMatchingService(),
                    headerMatcher: headerMatcher(),
                    bodyMatchingService: bodyMatchingService(),
                    objectMapper: objectMapper(),
                    variableStore: variableStore()
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
                    jsonBodyMatcher : jsonBodyMatcher()
            )
        }
        return bodyMatchService
    }

    public static JsonBodyMatcher jsonBodyMatcher() {
        if(jsonBodyMatcher == null) {}
            jsonBodyMatcher = new JsonBodyMatcher(
                objectMapper: objectMapper(),
                expressionMatcher: expressionMatcher()
        )
        return jsonBodyMatcher
    }

    public static EnricherService enricherService() {
        if (enricherService == null) {
            enricherService = new EnricherService(
                variableStore: variableStore()
            )
        }
        return enricherService
    }

    public static ExpressionMatcher expressionMatcher() {
        if (expressionMatcher == null) {
            expressionMatcher = new ExpressionMatcher()
        }
        return expressionMatcher
    }

    public static LocalConfigurationSource localConfigurationSource() {
        if (localConfigurationSource == null) {
            localConfigurationSource = new LocalConfigurationSource()
        }
        return localConfigurationSource
    }

    public static VariableStore variableStore() {
        if(variableStore == null) {
            variableStore = new VariableStore(
                expressionMatcher: expressionMatcher(),
                stringVariableExtractor: new StringVariableExtractor(),
                jsonBodyVariableExtractor: jsonBodyVariableExtractor()
            )
        }
        return variableStore
    }

    public static JsonBodyVariableExtractor jsonBodyVariableExtractor() {
        if (jsonBodyVariableExtractor == null) {
            jsonBodyVariableExtractor = new JsonBodyVariableExtractor(
                    objectMapper: objectMapper(),
                    stringVariableExtractor: new StringVariableExtractor(),
                    jsonBodyMatcher: jsonBodyMatcher()
            )
        }
        return jsonBodyVariableExtractor
    }
}
