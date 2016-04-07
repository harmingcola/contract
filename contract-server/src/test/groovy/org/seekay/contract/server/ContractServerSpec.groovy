package org.seekay.contract.server

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.catalina.LifecycleException
import org.apache.catalina.startup.Tomcat
import org.seekay.contract.model.ContractTestFixtures
import org.seekay.contract.model.util.Http
import spock.lang.Specification

class ContractServerSpec extends Specification {

    def "a server should be start-able with a single contract" () {
        given:
            ContractServer server = ContractServer.newServer().onRandomPort().startServer()
        when:
            server.addContract(ContractTestFixtures.defaultGetContract().noRequestHeaders().build())
            def statusCode = Http.get().toPath(server.path() + "/builder/2").execute().statusCode
        then:
            statusCode == 200
        cleanup:
            server.stopServer()
    }

    def "a server should be start-able with multiple contracts" () {
        given:
            ContractServer server = ContractServer.newServer().onRandomPort().startServer()
        when:
            server.addContracts(
                ContractTestFixtures.defaultGetContract().noRequestHeaders().build(),
                ContractTestFixtures.defaultPostContract().noRequestHeaders().build()
            )
            def statusCode = Http.get().toPath(server.path() + "/builder/2").execute().statusCode
        then:
            statusCode == 200
        cleanup:
            server.stopServer()
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
        cleanup:
            server.stopServer()
    }

    def "a server should be start-able from local resources" () {
        given:
            ContractServer server = ContractServer.newServer().onRandomPort().withLocalConfig("src/test/resources/contracts").startServer()
        when:
            def statusCode = Http.get().toPath(server.path() + "/server-unit-tests/1").withHeaders(["key":"value"]).execute().statusCode
        then:
            statusCode == 203
        cleanup:
            server.stopServer()
    }

    def "a server should be start-able from public git repository" () {
        given:
            ContractServer server = ContractServer.newServer().onRandomPort().withGitConfig("https://bitbucket.org/harmingcola/contract-test-public").startServer()
        when:
            def statusCode = Http.get().toPath(server.path() + "/entity/1").withHeaders(["key":"value"]).execute().statusCode
        then:
            statusCode == 200
        cleanup:
            server.stopServer()
    }

    def "a server should be start-able from private git repository" () {
        given:
            ContractServer server = ContractServer.newServer().onRandomPort().withGitConfig("https://bitbucket.org/harmingcola/contract-test-private", 'seekay_test', 'seekay_test_password').startServer()
        when:
            def statusCode = Http.get().toPath(server.path() + "/entity/1").withHeaders(["key":"value"]).execute().statusCode
        then:
            statusCode == 200
        cleanup:
            server.stopServer()
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
        cleanup:
            server.stopServer()
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
}
