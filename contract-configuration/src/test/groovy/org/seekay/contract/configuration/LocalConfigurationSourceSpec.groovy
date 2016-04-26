package org.seekay.contract.configuration
import com.fasterxml.jackson.databind.ObjectMapper
import org.seekay.contract.model.domain.Contract
import spock.lang.Specification

import static org.seekay.contract.model.domain.Method.GET
import static org.seekay.contract.model.util.ListTools.first

class LocalConfigurationSourceSpec extends Specification {

    def "contracts in a local directory should be loadable" () {
        given:
            ConfigurationSource source = new LocalConfigurationSource("src/test/resources/contracts/simpleLoadTest")
        when:
            List<Contract> contracts = source.load()
        then:
            contracts.size() == 1
    }

    def "contracts should be loaded correctly" () {
        given:
            ConfigurationSource source = new LocalConfigurationSource("src/test/resources/contracts/simpleLoadTest")
        when:
            List<Contract> contracts = source.load()
            Contract contract = first(contracts)
        then:
            contract.request.method == GET
            contract.request.path == "/entity/1"
            contract.request.headers["key"] == "value"
            contract.response.status == 200
            contract.response.body == "hello world"
    }

    def "multiple contracts can be loaded from a single directory" () {
        given:
            ConfigurationSource source = new LocalConfigurationSource("src/test/resources/contracts/multipleGetContracts")
        when:
            List<Contract> contracts = source.load()
        then:
            contracts.size() == 2
            contracts.collect { contract ->
                assert contract.response.body == "hello world"
                assert contract.response.status == 200
            }
    }

    def "multiple contracts can be loaded from arbitrary folder depths" () {
        given:
            ConfigurationSource source = new LocalConfigurationSource("src/test/resources/contracts/crazyFolderLayout")
        when:
            List<Contract> contracts = source.load()
        then:
            contracts.size() == 2
            contracts.collect { contract ->
                assert contract.response.body == "hello world"
                assert contract.response.status == 200
            }
    }

    def "when an exception is thrown loading a contract, it should be logged" () {
        given:
            ConfigurationSource source = new LocalConfigurationSource("src/test/resources/contracts/crazyFolderLayout")
            ObjectMapper objectMapper = Mock(ObjectMapper)
            source.objectMapper = objectMapper
        when:
            source.load()
        then:
            objectMapper.readValue(_, HashMap.class) >> {throw new IOException()}
			thrown(IllegalStateException)
    }

	def "tags should be generated correctly based on directory structure" () {
		given:
			ConfigurationSource source = new LocalConfigurationSource("src/test/resources/")
		when:
			List<Contract> contracts = source.load()
			Contract contract = first(contracts)
			Set tags = contract.info['tags']
		then:
			tags.size() == 2
			tags.contains('contracts')
			tags.contains('simpleloadtest')
	}
}
