package org.seekay.contract.server.servlet
import com.fasterxml.jackson.databind.ObjectMapper
import org.seekay.contract.model.domain.Contract
import org.seekay.contract.common.builder.ContractBuilder
import org.seekay.contract.server.servet.ConfigurationServlet
import org.seekay.contract.common.service.ContractService
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import spock.lang.Shared
import spock.lang.Specification

import static org.seekay.contract.model.ContractTestFixtures.*

class ConfigurationServletSpec extends Specification {

    @Shared
    ConfigurationServlet servlet = new ConfigurationServlet()

    ContractService contractService = Mock(ContractService)
    ContractBuilder contractBuilder = Mock(ContractBuilder)
    ObjectMapper objectMapper = new ObjectMapper()

    def setup() {
        servlet.contractService = contractService
        servlet.contractBuilder = contractBuilder
        servlet.objectMapper = objectMapper
    }

    def "init should setup the servlet correctly"() {
        given:
            ConfigurationServlet configurationServlet = new ConfigurationServlet()
        when:
            configurationServlet.init()
        then:
            configurationServlet.contractService != null
            configurationServlet.contractBuilder != null
            configurationServlet.objectMapper != null
    }

    def "a get request to the servlet should return the current contracts"() {
        given:
            MockHttpServletRequest request = new MockHttpServletRequest()
            MockHttpServletResponse response = new MockHttpServletResponse()
            1 * contractService.read() >> {oneDefaultContractOfEachMethod()}
        when:
            servlet.doGet(request, response)
        then:
            response.getContentAsString().contains("""{"request":{"method":"GET","path":"/bu""")
            response.getContentAsString().contains(""""headers":{"captain":"america"}""")
    }

    def "if no contracts have been defined and a request if received, an error message should be returned" () {
        given:
            MockHttpServletRequest request = new MockHttpServletRequest()
            MockHttpServletResponse response = new MockHttpServletResponse()
            1 * contractService.read() >> {[]}
        when:
            servlet.doGet(request, response)
        then:
            response.getContentAsString() == ConfigurationServlet.NO_PACTS_HAVE_BEEN_DEFINED
    }

    def "a post request containing a contract should add it to the contract service" () {
        given:
            Contract contract = defaultPostContract()build()
            byte[] contractJson = objectMapper.writeValueAsBytes(contract)
            MockHttpServletRequest request = new MockHttpServletRequest()
            request.setContent(contractJson)
            MockHttpServletResponse response = new MockHttpServletResponse()
        when:
            servlet.doPost(request, response)
        then:
            response.status == 201
            response.contentAsString.startsWith("""{"request":{"method":"GET""")
            1 * contractBuilder.fromJson(_ as String) >> {defaultGetContract().build()}
    }
}
