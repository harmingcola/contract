package org.seekay.contract.configuration.loaders

import com.fasterxml.jackson.databind.ObjectMapper
import org.seekay.contract.model.domain.Contract
import org.seekay.contract.model.domain.Method
import spock.lang.Specification


class StringBodyJsonFileLoaderSpec extends Specification {

	File file = new File('src/test/resources/contracts/simpleLoadTest/get-entity-1.contract.json')

	def 'a file should load into a contract correctly' () {
		given:
			StringBodyJsonFileLoader loader = new StringBodyJsonFileLoader(file)
		when:
			def contract = loader.load()
		then:
			contract.request.method == Method.GET
			contract.request.path == '/entity/1'
			contract.request.headers['key'] == 'value'
			contract.response.status == '200'
			contract.response.body == 'hello world'
	}

	def 'an IOException caught during json processing should be rethrown as an IllegalStateException' () {
		given:
			StringBodyJsonFileLoader loader = new StringBodyJsonFileLoader(file)
			ObjectMapper objectMapper = Mock(ObjectMapper)
			loader.objectMapper = objectMapper
		when:
			loader.load()
		then:
			1 * objectMapper.readValue(_, Contract.class) >> {throw new IOException()}
			thrown(IllegalStateException)
	}

	def 'the fileName should be loaded into the info block' () {
		given:
			StringBodyJsonFileLoader loader = new StringBodyJsonFileLoader(file)
		when:
			def contract = loader.load()
		then:
			contract.info['fileName'] == 'get-entity-1.contract.json'
	}
}
