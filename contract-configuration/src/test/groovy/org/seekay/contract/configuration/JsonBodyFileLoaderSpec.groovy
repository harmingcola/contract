package org.seekay.contract.configuration

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.ObjectMapper
import org.seekay.contract.model.domain.ContractMap
import org.seekay.contract.model.domain.Method
import spock.lang.Specification

class JsonBodyFileLoaderSpec extends Specification {

    ContractMap contents = new ContractMap(
            setup: [
                [
                    request: [
                            path  : "/context/resource",
                            method: "PUT",
                            body  : [
                                    "steve": "rogers"
                            ]
                    ] as HashMap,
                    response: [
                            status: 201,
                            body  : [
                                    "matt": "murdock"
                            ]
                    ] as HashMap
                ] as HashMap
            ] as LinkedList,
            request: [
                    path  : "/context/resource",
                    method: "PUT",
                    body  : [
                            "steve": "rogers"
                    ]
            ] as HashMap,
            response: [
                    status: 201,
                    body  : [
                            "matt": "murdock"
                    ]
            ] as HashMap,
            filters: [
                    "filter1"
            ]
    )

    JsonBodyFileLoader loader = new JsonBodyFileLoader()

    def 'the contents hashmap should convert into a contract correctly'() {
        when:
            def contract = loader.load(contents)
        then:
            contract.request.path == '/context/resource'
            contract.request.method == Method.PUT
            contract.request.body == """{"steve":"rogers"}"""
            contract.response.status == '201'
            contract.response.body == """{"matt":"murdock"}"""
    }

    def 'when a JsonProcessingException is caught, it should be rethrown as an IllegalState'() {
        given:
            ObjectMapper objectMapper = Mock(ObjectMapper)
            loader.objectMapper = objectMapper
        when:
            loader.load(contents)
        then:
            1 * objectMapper.writeValueAsString(_) >> { throw new JsonParseException('Boom, broke', null) }
            thrown(IllegalStateException)
    }

    def 'when an IOException is caught, it should be rethrown as an IllegalState'() {
        given:
            ObjectMapper objectMapper = Mock(ObjectMapper)
            loader.objectMapper = objectMapper
        when:
            loader.load(contents)
        then:
            1 * objectMapper.writeValueAsString(_) >> { throw new IOException() }
            thrown(IllegalStateException)
    }

    def 'contracts in the setup block should have their bodies escaped'() {
        given:
            JsonBodyFileLoader loader = new JsonBodyFileLoader()
        when:
            def contract = loader.load(contents)
        then:
            contract.setup[0].response.body == """{"matt":"murdock"}"""
    }

    def 'filters should be loaded correctly' () {
        given:
            JsonBodyFileLoader loader = new JsonBodyFileLoader()
        when:
            def contract = loader.load(contents)
        then:
            contract.filters[0] == "filter1"
    }
}
