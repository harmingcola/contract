package org.seekay.contract.configuration.git
import org.eclipse.jgit.api.errors.CheckoutConflictException
import org.seekay.contract.configuration.ConfigurationSource
import org.seekay.contract.configuration.git.git.GitConfigurationSource
import org.seekay.contract.model.domain.Contract
import spock.lang.Specification

import static org.seekay.contract.model.util.ListTools.first

class GitConfigurationSourceSpec extends Specification {

    def setup() {
		File tempContractFileDirectory = new File(GitConfigurationSource.DOWNLOAD_LOCATION)
		if(tempContractFileDirectory.exists()) {
			tempContractFileDirectory.deleteDir()
		}
	}

    def "contract files can be downloaded from a git repo and unmarshalled into contract objects" () {
        given:
            ConfigurationSource source = new GitConfigurationSource("https://harmingcola@bitbucket.org/harmingcola/contract-test.git","harmingcola", "existenz")
        when:
            List<Contract> contracts = source.load()
            Contract contract = first(contracts)
        then:
            contracts.size() == 1
            contract.request.path == "/entity/1"
            contract.response.status == 200
    }

    def "the config source should delete an old source files before continuing" () {
        given:
            ConfigurationSource oldConfigurationSource = new GitConfigurationSource("https://harmingcola@bitbucket.org/harmingcola/contract-test.git","harmingcola", "existenz")
            oldConfigurationSource.load()
            ConfigurationSource newConfigurationSource = new GitConfigurationSource("https://harmingcola@bitbucket.org/harmingcola/contract-test.git","harmingcola", "existenz")
        when:
            List<Contract> contracts = newConfigurationSource.load()
            Contract contract = first(contracts)
        then:
            contracts.size() == 1
            contract.request.path == "/entity/1"
            contract.response.status == 200
    }

    def "a problem during cloning the repository should be thrown as an illegal state" () {
        given:
            ConfigurationSource source = Spy(GitConfigurationSource, constructorArgs:["https://harmingcola@bitbucket.org/harmingcola/contract-test.git","harmingcola", "existenz"])
            1 * source.cloneRepository(_ as File) >> {throw new CheckoutConflictException("Its broken yo")}
        when:
            source.load()
        then:
            thrown(IllegalStateException)
    }

    def "a problem during deleting existing contracts should be thrown as an illegal state" () {
        given:
            ConfigurationSource oldConfigurationSource = new GitConfigurationSource("https://harmingcola@bitbucket.org/harmingcola/contract-test.git","harmingcola", "existenz")
            oldConfigurationSource.load()
            ConfigurationSource source = Spy(GitConfigurationSource, constructorArgs:["https://harmingcola@bitbucket.org/harmingcola/contract-test.git","harmingcola", "existenz"])
            source.deleteDirectory(_ as File) >> {throw new IOException("Its broken yo")}
        when:
            source.load()
        then:
            thrown(IllegalStateException)
    }
}
