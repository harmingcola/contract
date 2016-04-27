package org.seekay.contract.server.util

import org.seekay.contract.model.domain.Method
import org.seekay.contract.model.domain.ContractRequest
import org.springframework.mock.web.MockHttpServletRequest
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest


class RequestReaderSpec extends Specification{

    def "a http request should map to a contract request correctly"() {
        given:
            MockHttpServletRequest request = new MockHttpServletRequest()
            request.setContent("hello world".getBytes())
            request.setPathInfo("/entity/100")
            request.addHeader("key", "value")
            request.setMethod("GET")
        when:
            ContractRequest contractRequest = RequestReader.from(request).toContractRequest()
        then:
            contractRequest.method == Method.GET
            contractRequest.headers["key"] == "value"
            contractRequest.getPath() == "/entity/100"
            contractRequest.getBody() == "hello world"
    }

    def "if query params are supplied they should be appended to the path"() {
        given:
            MockHttpServletRequest request = new MockHttpServletRequest()
            request.setContent("hello world".getBytes())
            request.setPathInfo("/entity/100")
            request.setQueryString("key=value")
            request.addHeader("key", "value")
            request.setMethod("GET")
        when:
            ContractRequest contractRequest = RequestReader.from(request).toContractRequest()
        then:
            contractRequest.method == Method.GET
            contractRequest.headers["key"] == "value"
            contractRequest.getPath() == "/entity/100?key=value"
            contractRequest.getBody() == "hello world"
    }

    def "a problem in converting a request to a contract request should be wrapped in an illegal state" () {
        given:
            HttpServletRequest request = Mock(HttpServletRequest)
            request.getReader() >> {throw new IOException()}
        when:
            RequestReader.from(request).readBody()
        then:
            thrown(IllegalStateException)
    }
}
