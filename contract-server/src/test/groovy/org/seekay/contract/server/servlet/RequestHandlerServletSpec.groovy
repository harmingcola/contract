package org.seekay.contract.server.servlet

import org.seekay.contract.model.domain.ContractRequest
import org.seekay.contract.server.servet.RequestHandlerServlet
import org.seekay.contract.server.service.MatchingService
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import spock.lang.Shared
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import static org.seekay.contract.model.ContractTestFixtures.defaultDeleteContract
import static org.seekay.contract.model.ContractTestFixtures.defaultGetContract
import static org.seekay.contract.model.ContractTestFixtures.defaultPostContract
import static org.seekay.contract.model.ContractTestFixtures.defaultPutContract

class RequestHandlerServletSpec extends Specification {

    @Shared
    RequestHandlerServlet servlet = new RequestHandlerServlet()

    MatchingService matchingService = Mock(MatchingService)

    def setup() {
        servlet.matchingService = matchingService
    }

    def "the init method should setup the application correctly" () {
        given:
            RequestHandlerServlet requestHandlerServlet = new RequestHandlerServlet()
        when:
            requestHandlerServlet.init()
        then:
            requestHandlerServlet.matchingService != null
    }

    def "a GET request should be handled correctly" () {
        given:
            HttpServletRequest request = buildServletRequest()
            HttpServletResponse response = new MockHttpServletResponse()
            1 * matchingService.matchGetRequest(_ as ContractRequest) >> {defaultGetContract().build()}
        when:
            servlet.doGet(request, response)
        then:
            response.getHeaderValue("captain") == "america"
            response.getStatus() == 200
            response.getContentAsString() == "hello world"
    }

    def "a POST request should be handled correctly" () {
        given:
            HttpServletRequest request = buildServletRequest()
            HttpServletResponse response = new MockHttpServletResponse()
            1 * matchingService.matchPostRequest(_ as ContractRequest) >> {defaultPostContract().build()}
        when:
            servlet.doPost(request, response)
        then:
            response.getHeaderValue("incredible") == "hulk"
            response.getStatus() == 200
            response.getContentAsString() == "I like cheese"
    }

    def "a PUT request should be handled correctly" () {
        given:
            HttpServletRequest request = buildServletRequest()
            HttpServletResponse response = new MockHttpServletResponse()
            1 * matchingService.matchPutRequest(_ as ContractRequest) >> {defaultPutContract().build()}
        when:
            servlet.doPut(request, response)
        then:
            response.getHeaderValue("war") == "machine"
            response.getStatus() == 200
            response.getContentAsString() == "I like eggs"
    }

    def "a DELETE request should be handled correctly" () {
        given:
            HttpServletRequest request = buildServletRequest()
            HttpServletResponse response = new MockHttpServletResponse()
            1 * matchingService.matchDeleteRequest(_ as ContractRequest) >> {defaultDeleteContract().build()}
        when:
            servlet.doDelete(request, response)
        then:
            response.getStatus() == 204
    }

    def "if no matching contracts are found, a NOT_FOUND and error message should be returned" () {
        given:
            HttpServletRequest request = buildServletRequest()
            HttpServletResponse response = new MockHttpServletResponse()
            1 * matchingService.matchPostRequest(_ as ContractRequest) >> null
        when:
            servlet.doPost(request, response)
        then:
            response.getStatus() == 404
            response.getContentAsString() == RequestHandlerServlet.NO_MATCHING_PACTS_FOUND
    }


    HttpServletRequest buildServletRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest()
        request.setContent("hello world".getBytes())
        request.setPathInfo("/entity/100")
        request.addHeader("key", "value")
        request.setMethod("GET")
        request.setContent("I'm a body".getBytes())
        return request
    }
}
