package org.seekay.contract.configuration.loaders
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.ObjectMapper
import org.seekay.contract.model.domain.Method
import spock.lang.Specification

class JsonBodyFileLoaderSpec extends Specification {

	def contents = [
		request : [
			path: "/context/resource",
			method : "PUT",
			body: [
				"steve":"rogers"
			]
		],
		response : [
			status : 201,
			body: [
				"matt":"murdock"
			]
		]
	]

	JsonBodyFileLoader loader = new JsonBodyFileLoader(contents, new File("src/test/resources/contracts/simpleLoadTest/get-entity-1.contract.json"))

	def 'the contents hashmap should convert into a contract correctly' () {
		when:
			def contract = loader.load()
		then:
			contract.request.path == '/context/resource'
			contract.request.method == Method.PUT
			contract.request.body == """{"steve":"rogers"}"""
			contract.response.status == 201
			contract.response.body == """{"matt":"murdock"}"""
	}

	def 'when a JsonProcessingException is caught, it should be rethrown as an IllegalState' () {
		given:
			ObjectMapper objectMapper = Mock(ObjectMapper)
			loader.objectMapper = objectMapper
		when:
			loader.load()
		then:
			1 * objectMapper.writeValueAsString(_) >> {throw new JsonParseException('Boom, broke', null)}
			thrown(IllegalStateException)
	}

	def 'when an IOException is caught, it should be rethrown as an IllegalState' () {
		given:
			ObjectMapper objectMapper = Mock(ObjectMapper)
			loader.objectMapper = objectMapper
		when:
			loader.load()
		then:
			1 * objectMapper.writeValueAsString(_) >> {throw new IOException()}
			thrown(IllegalStateException)
	}

	def 'the fileName should be loaded into the info block' () {
		when:
			def contract = loader.load()
		then:
			contract.info['fileName'] == 'get-entity-1.contract.json'
	}
}
