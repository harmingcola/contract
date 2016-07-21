package org.seekay.contract.server.servlet

import org.seekay.contract.common.enrich.EnricherService
import org.seekay.contract.common.match.MatchingService
import org.seekay.contract.common.service.ContractService
import org.seekay.contract.model.domain.Contract
import org.seekay.contract.model.domain.ContractRequest
import org.seekay.contract.server.servet.RequestHandlerServlet
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import spock.lang.Shared
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import static org.seekay.contract.model.ContractTestFixtures.*

class RequestHandlerServletSpec extends Specification {

    @Shared
    RequestHandlerServlet servlet = new RequestHandlerServlet()

    MatchingService matchingService = Mock(MatchingService)
    EnricherService enricherService = Mock(EnricherService)
    ContractService contractService = Mock(ContractService)

    def setup() {
        servlet.matchingService = matchingService
        servlet.enricherService = enricherService
        servlet.contractService = contractService
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
            1 * enricherService.enrichResponse(_ as Contract) >> {defaultGetContract().build()}
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
            1 * enricherService.enrichResponse(_ as Contract) >> {defaultPostContract().build()}
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
            1 * enricherService.enrichResponse(_ as Contract) >> {defaultPutContract().build()}
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
            1 * enricherService.enrichResponse(_ as Contract) >> {defaultDeleteContract().build()}
        when:
            servlet.doDelete(request, response)
        then:
            response.getStatus() == 204
    }

    def "a HEAD request should be handled correctly" () {
        given:
            HttpServletRequest request = buildServletRequest()
            HttpServletResponse response = new MockHttpServletResponse()
            1 * matchingService.matchHeadRequest(_ as ContractRequest) >> {defaultHeadContract().build()}
            1 * enricherService.enrichResponse(_ as Contract) >> {defaultHeadContract().build()}
        when:
            servlet.doHead(request, response)
        then:
            response.getStatus() == 200
    }

    def "an OPTIONS request should be handled correctly" () {
        given:
            HttpServletRequest request = buildServletRequest()
            HttpServletResponse response = new MockHttpServletResponse()
            1 * matchingService.matchOptionsRequest(_ as ContractRequest) >> {defaultOptionsContract().build()}
            1 * enricherService.enrichResponse(_ as Contract) >> {defaultOptionsContract().build()}
        when:
            servlet.doOptions(request, response)
        then:
            response.getStatus() == 200
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
