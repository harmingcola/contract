package org.seekay.contract.configuration.loaders

import com.fasterxml.jackson.databind.ObjectMapper
import org.seekay.contract.model.domain.Contract
import org.seekay.contract.model.domain.Method
import spock.lang.Specification


class StringBodyJsonFileLoaderSpec extends Specification {

	def contractDefinition = '''
		{
		  "request" : {
			"method" : "GET",
			"path" : "/entity/1",
			"headers": {
			  "key" : "value"
			}
		  },
		  "response" : {
			"status" : 200,
			"body" : "hello world"
		  }
		}
	'''

	def 'a file should load into a contract correctly' () {
		given:
			StringBodyJsonFileLoader loader = new StringBodyJsonFileLoader()
		when:
			def contract = loader.load(contractDefinition)
		then:
			contract.request.method == Method.GET
			contract.request.path == '/entity/1'
			contract.request.headers['key'] == 'value'
			contract.response.status == '200'
			contract.response.body == 'hello world'
	}

	def 'an IOException caught during json processing should be rethrown as an IllegalStateException' () {
		given:
			StringBodyJsonFileLoader loader = new StringBodyJsonFileLoader()
			ObjectMapper objectMapper = Mock(ObjectMapper)
			loader.objectMapper = objectMapper
		when:
			loader.load(contractDefinition)
		then:
			1 * objectMapper.readValue(_, Contract.class) >> {throw new IOException()}
			thrown(IllegalStateException)
	}
}
