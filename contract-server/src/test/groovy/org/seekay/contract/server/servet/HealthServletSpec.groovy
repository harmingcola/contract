package org.seekay.contract.server.servet

import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import spock.lang.Shared
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import static org.seekay.contract.server.servet.HealthServlet.*

class HealthServletSpec extends Specification {

    @Shared HealthServlet servlet = new HealthServlet()

    def 'a get request should return a 200 and a health message' () {
        given:
            MockHttpServletRequest request = buildServletRequest()
            request.setMethod("GET")
            HttpServletResponse response = new MockHttpServletResponse()
        when:
            servlet.doGet(request, response)
        then:
            response.getStatus() == 200
            response.getContentAsString() == SERVER_RUNNING
    }

    HttpServletRequest buildServletRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest()
        request.setContent("hello world".getBytes())
        request.setPathInfo("/entity/100")
        request.addHeader("key", "value")
        request.setContent("I'm a body".getBytes())
        return request
    }
}
