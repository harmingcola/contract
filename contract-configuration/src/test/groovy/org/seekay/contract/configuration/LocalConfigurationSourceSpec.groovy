package org.seekay.contract.configuration
import com.fasterxml.jackson.databind.ObjectMapper
import org.seekay.contract.model.domain.Contract
import spock.lang.Specification

import static org.seekay.contract.model.domain.Method.GET

class LocalConfigurationSourceSpec extends Specification {

    def "contracts in a local directory should be loadable" () {
        given:
            ConfigurationSource source = new LocalConfigurationSource("src/test/resources/contracts/")
        when:
            List<Contract> contracts = source.load()
        then:
            contracts.size() == 13
    }

    def "contracts should be loaded correctly" () {
        given:
            ConfigurationSource source = new LocalConfigurationSource("src/test/resources/contracts/simpleLoadTest")
        when:
            List<Contract> contracts = source.load()
            Contract contract = contracts.find() {it.request.path == "/entity/1"}
        then:
            contract.request.method == GET
            contract.request.headers["key"] == "value"
            contract.response.status == '200'
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
                assert contract.response.status == '200'
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
                assert contract.response.status == '200'
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

//	def "tags should be generated correctly based on directory structure" () {
//		given:
//			ConfigurationSource source = new LocalConfigurationSource("src/test/resources/contracts/simpleLoadTest")
//		when:
//			List<Contract> contracts = source.load()
//			Contract contract = contracts.find() {it.readTags().size() > 0 }
//			Set tags = contract.info['tags']
//		then:
//			tags.size() == 1
//			tags.contains('simpleloadtest')
//	}

    def 'no arg constructor should work correctly' () {
        given:
            def source = new LocalConfigurationSource();
        expect:
            source != null
    }

    def 'ignored directories should be ignored' () {
        given:
            ConfigurationSource source = new LocalConfigurationSource("src/test/resources/contracts")
        when:
            List<Contract> contracts = source.load()
            Contract contract = contracts.find() {it.request.path == "/entity/15"}
        then:
            contract == null
    }

    def 'contracts with mismatched body types will throw an exception' () {
        given:
            ConfigurationSource source = new LocalConfigurationSource("src/test/resources/invalid_contracts")
        when:
            source.load()
        then:
            thrown(IllegalStateException)
    }

    def "an IOException thrown during reading a file should be rethrown as an illegal state" () {
        given:
            ConfigurationSource source = new LocalConfigurationSource()
            File file = Mock(File)
        when:
            source.loadFromFile(file)
        then:
            thrown(IllegalStateException)
            1 * file.getName() >> {return "sample.contract.json"}
            1 * file.getAbsolutePath() >> {throw new IOException("")}
    }
}
