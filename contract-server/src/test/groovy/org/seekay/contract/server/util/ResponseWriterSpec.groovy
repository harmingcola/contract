package org.seekay.contract.server.util

import org.springframework.mock.web.MockHttpServletResponse
import spock.lang.Specification

import javax.servlet.http.HttpServletResponse

class ResponseWriterSpec extends Specification {

    def 'headers should be written to the response' () {
        given:
            HttpServletResponse response = new MockHttpServletResponse()
        when:
            ResponseWriter.to(response).headers(['one':'two','three':'four']).write('')
        then:
            response.getHeader('one') == 'two'
            response.getHeader('three') == 'four'
    }

    def 'the body should be written to the response' () {
        given:
            HttpServletResponse response = new MockHttpServletResponse()
        when:
            ResponseWriter.to(response).write('hello world')
        then:
            response.getContentAsString() == 'hello world'
    }

    def 'ok status should be written to the response' () {
        given:
            HttpServletResponse response = new MockHttpServletResponse()
        when:
            ResponseWriter.to(response).ok().write('')
        then:
            response.getStatus() == 200
    }

    def 'created status should be written to the response' () {
        given:
            HttpServletResponse response = new MockHttpServletResponse()
        when:
            ResponseWriter.to(response).created().write('')
        then:
            response.getStatus() == 201
    }

    def 'not found status should be written to the response' () {
        given:
            HttpServletResponse response = new MockHttpServletResponse()
        when:
            ResponseWriter.to(response).notFound().write('')
        then:
            response.getStatus() == 404
    }

    def 'any status code should be written to the response' () {
        given:
            HttpServletResponse response = new MockHttpServletResponse()
        when:
            ResponseWriter.to(response).status('101').write('')
        then:
            response.getStatus() == 101
    }

    def 'IOExceptions thrown when writing the response should be thrown as an IllegalState'() {
        given:
            HttpServletResponse response = Mock(HttpServletResponse)
            response.getOutputStream() >> {throw new IOException()}
        when:
            ResponseWriter.to(response).write('break this!!')
        then:
            thrown(IllegalStateException)
    }
}
