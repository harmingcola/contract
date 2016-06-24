package org.seekay.contract.configuration
import com.fasterxml.jackson.databind.ObjectMapper
import org.seekay.contract.model.domain.Contract
import org.seekay.contract.model.domain.ContractMap
import spock.lang.Specification

import static org.seekay.contract.model.domain.Method.GET

class LocalConfigurationSourceSpec extends Specification {

    def "contracts in a local directory should be loadable" () {
        given:
            LocalConfigurationSource source = new LocalConfigurationSource()
        when:
            List<Contract> contracts = source.loadFromDirectory("src/test/resources/contracts/")
        then:
            contracts.size() == 14
    }

    def "contracts should be loaded correctly" () {
        given:
            LocalConfigurationSource source = new LocalConfigurationSource()
        when:
            List<Contract> contracts = source.loadFromDirectory("src/test/resources/contracts/simpleLoadTest")
            Contract contract = contracts.find() {it.request.path == "/entity/1"}
        then:
            contract.request.method == GET
            contract.request.headers["key"] == "value"
            contract.response.status == '200'
            contract.response.body == "hello world"
    }

    def "multiple contracts can be loaded from a single directory" () {
        given:
            LocalConfigurationSource source = new LocalConfigurationSource()
        when:
            List<Contract> contracts = source.loadFromDirectory("src/test/resources/contracts/multipleGetContracts")
        then:
            contracts.size() == 2
            contracts.collect { contract ->
                assert contract.response.body == "hello world"
                assert contract.response.status == '200'
            }
    }

    def "multiple contracts can be loaded from arbitrary folder depths" () {
        given:
            LocalConfigurationSource source = new LocalConfigurationSource()
        when:
            List<Contract> contracts = source.loadFromDirectory("src/test/resources/contracts/crazyFolderLayout")
        then:
            contracts.size() == 2
            contracts.collect { contract ->
                assert contract.response.body == "hello world"
                assert contract.response.status == '200'
            }
    }

    def "when an exception is thrown loading a contract, it should be logged" () {
        given:
            LocalConfigurationSource source = new LocalConfigurationSource()
            ObjectMapper objectMapper = Mock(ObjectMapper)
            source.objectMapper = objectMapper
        when:
            source.loadFromDirectory("src/test/resources/contracts/crazyFolderLayout")
        then:
            objectMapper.readValue(_, ContractMap.class) >> {throw new IOException()}
			thrown(IllegalStateException)
    }

	def "tags should be generated correctly based on directory structure" () {
		given:
            LocalConfigurationSource source = new LocalConfigurationSource()
		when:
			List<Contract> contracts = source.loadFromDirectory("src/test/resources/contracts")
			Contract contract = contracts.sort().find() {it.readTags().size() > 0 }
			Set tags = contract.info['tags']
			tags.sort()
		then:
			tags.size() == 1
			tags.contains('simpleloadtest')
	}

    def 'no arg constructor should work correctly' () {
        given:
            def source = new LocalConfigurationSource();
        expect:
            source != null
    }

    def 'ignored directories should be ignored' () {
        given:
            LocalConfigurationSource source = new LocalConfigurationSource()
        when:
            List<Contract> contracts = source.loadFromDirectory("src/test/resources/contracts")
            Contract contract = contracts.find() {it.request.path == "/entity/15"}
        then:
            contract == null
    }

    def 'contracts containing a setup block will load correctly' () {
        given:
           LocalConfigurationSource source = new LocalConfigurationSource()
        when:
            def contracts = source.loadFromDirectory("src/test/resources/contracts/setupBlock")
            Contract contract = contracts.find() {it.request.path == "/entity/16"}
        then:
            contract.setup.size() == 1
    }

    def "an IOException thrown during reading a file should be rethrown as an illegal state" () {
        given:
            LocalConfigurationSource source = new LocalConfigurationSource()
            File file = Mock(File)
        when:
            source.loadFromFile(file)
        then:
            thrown(IllegalStateException)
            1 * file.getName() >> {return "sample.contract.json"}
            1 * file.getAbsolutePath() >> {throw new IOException("")}
    }
}
