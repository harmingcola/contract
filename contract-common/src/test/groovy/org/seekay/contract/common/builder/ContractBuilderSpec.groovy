package org.seekay.contract.common.builder

import com.fasterxml.jackson.databind.ObjectMapper
import org.seekay.contract.model.domain.Method
import org.seekay.contract.model.domain.Contract
import spock.lang.Specification


class ContractBuilderSpec extends Specification {

    def contractJson = """{
        "request" : {
            "method" : "GET",
            "path" : "/entity/1",
            "body" : "what do ya say world?",
            "headers" : {
                "one" : "two"
            }
        },
        "response" : {
            "status" : 200,
            "body" : "hello world!",
            "headers" : {
                "three" : "four"
            }
        }
    }"""

    def "a json contract should be converted correctly" () {
        given:
            ContractBuilder builder = new ContractBuilder()
            builder.setObjectMapper(new ObjectMapper())
        when:
            Contract contract = builder.fromJson(contractJson)
        then:
            contract.request.method == Method.GET
            contract.request.path == "/entity/1"
            contract.request.body == "what do ya say world?"
            contract.request.headers["one"] == "two"

            contract.response.status == '200'
            contract.response.body == "hello world!"
            contract.response.headers["three"] == "four"
    }

    def "an exception thrown during json conversion should be wrapped in an illegal state" () {
        given:
            ObjectMapper objectMapper = Mock(ObjectMapper)
            objectMapper.readValue(_ as String, Contract.class) >> {throw new IOException()}
            ContractBuilder builder = new ContractBuilder()
            builder.setObjectMapper(objectMapper)
        when:
            builder.fromJson(contractJson)
        then:
            thrown(IllegalStateException)
    }
}
