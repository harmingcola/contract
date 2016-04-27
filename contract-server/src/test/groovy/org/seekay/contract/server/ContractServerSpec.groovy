package org.seekay.contract.server
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.catalina.LifecycleException
import org.apache.catalina.startup.Tomcat
import org.seekay.contract.model.ContractTestFixtures
import org.seekay.contract.model.tools.Http

import static org.seekay.contract.model.ContractTestFixtures.defaultGetContract

class ContractServerSpec extends ClientFacingTest {

    def "a server should be start-able with a single contract" () {
        when:
            server.addContract(ContractTestFixtures.defaultGetContract().noRequestHeaders().build())
            def statusCode = Http.get().toPath(server.path() + "/builder/2").execute().statusCode
        then:
            statusCode == 200
    }

    def "a server should be start-able with multiple contracts" () {
        when:
            server.addContracts(
                ContractTestFixtures.defaultGetContract().noRequestHeaders().build(),
                ContractTestFixtures.defaultPostContract().noRequestHeaders().build()
            )
            def statusCode = Http.get().toPath(server.path() + "/builder/2").execute().statusCode
        then:
            statusCode == 200
    }

    def "a server should be start-able on a specific port" () {
        given:
            def port = 9501
            ContractServer server = ContractServer.newServer().onPort(port).startServer()
        when:
            server.addContract(ContractTestFixtures.defaultGetContract().noRequestHeaders().build())
            def statusCode = Http.get().toPath("http://localhost:"+port+"/builder/2").execute().statusCode
        then:
            statusCode == 200
    }

    def "a server should be start-able from local resources" () {
        given:
            server.withLocalConfig("src/test/resources/contracts").pushContractsToServer()
        when:
            def statusCode = Http.get().toPath(server.path() + "/server-unit-tests/1").withHeaders(["key":"value"]).execute().statusCode
        then:
            statusCode == 203
    }

    def "a server should be start-able from public git repository" () {
        given:
            server.withGitConfig("https://bitbucket.org/harmingcola/contract-test-public")
                    .pushContractsToServer()
        when:
            def statusCode = Http.get().toPath(server.path() + "/entity/1").withHeaders(["key":"value"]).execute().statusCode
        then:
            statusCode == 200
    }

    def "a server should be start-able from private git repository" () {
        given:
            server.withGitConfig("https://bitbucket.org/harmingcola/contract-test-private", 'seekay_test', 'seekay_test_password')
                    .pushContractsToServer()
        when:
            def statusCode = Http.get().toPath(server.path() + "/entity/1").withHeaders(["key":"value"]).execute().statusCode
        then:
            statusCode == 200
    }

    def "an error in json processing should be thrown as an illegal state exception" () {
        given:
            ContractServer server = ContractServer.newServer().onRandomPort().startServer()
            ObjectMapper objectMapper = Mock(ObjectMapper)
            objectMapper.writeValueAsString(_) >> {throw new JsonProcessingException('')}
            server.objectMapper = objectMapper
        when:
            server.addContract(ContractTestFixtures.defaultGetContract().noRequestHeaders().build())
        then:
            thrown(IllegalStateException)
    }

    def "a tomcat lifecycle exception should be rethrown as an IllegalState" () {
        given:
            ContractServer server = ContractServer.newServer().onRandomPort()
            Tomcat tomcat = Spy(Tomcat)
            tomcat.start() >> {throw new LifecycleException()}
            server.tomcat = tomcat
        when:
            server.startServer()
        then:
            thrown(IllegalStateException)
    }

    def "a server should be able to run contracts with certain tags" () {
        given:
            def contracts = [
                    defaultGetContract().tags("one", "delete").build(),
                    defaultGetContract().tags("two", "delete").build(),
                    defaultGetContract().tags("three", "delete").build()
            ]
            def contractServer = ContractServer.fromContracts(contracts)
        when:
            contractServer.onlyIncludeTags("three")
        then:
            contractServer.contracts.size() == 1
    }

    def "a client should not run contracts with certain tags" () {
        given:
            def contracts = [
                    defaultGetContract().tags("one", "delete").build(),
                    defaultGetContract().tags("two", "delete").build(),
                    defaultGetContract().tags("three", "delete").build()
            ]
            def contractServer = ContractServer.fromContracts(contracts)
        when:
            contractServer.excludeTags("two")
        then:
            contractServer.contracts.size() == 2
    }

    def "a client should be able to both include and exclude features with one call" () {
        given:
            def contracts = [
                    defaultGetContract().tags("one", "delete").build(),
                    defaultGetContract().tags("two", "delete").build(),
                    defaultGetContract().tags("three", "delete").build(),
                    defaultGetContract().tags("four", "get").build()
            ]
            def contractServer = ContractServer.fromContracts(contracts)
        when:
            contractServer.tags(["delete"] as Set, ["one", "three"] as Set)
        then:
            contractServer.contracts.size() == 1
    }
}
