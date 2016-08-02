package org.seekay.contract.server.servet

import com.fasterxml.jackson.databind.ObjectMapper
import org.seekay.contract.common.service.ContractService
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import spock.lang.Shared
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class FilterServletSpec extends Specification {

    @Shared FilterServlet servlet = new FilterServlet()

    ContractService contractService = Mock(ContractService)

    def setup() {
        servlet.contractService = contractService
        servlet.objectMapper = new ObjectMapper()
    }

    def 'a post to set filters should call the contract service correctly' () {
        given:
            MockHttpServletRequest request = buildServletRequest()
            HttpServletResponse response = new MockHttpServletResponse()
        when:
            servlet.doPost(request, response)
        then:
            1 * contractService.enableFilters(["no-servers-defined", "repositories-defined"])
    }

    HttpServletRequest buildServletRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest()
        request.setContent('''["no-servers-defined", "repositories-defined"]'''.getBytes())
        request.setMethod("GET")
        return request
    }
}
