package org.seekay.contract.configuration
import org.eclipse.jgit.api.errors.CheckoutConflictException
import org.seekay.contract.model.domain.Contract
import spock.lang.Specification

import static org.seekay.contract.model.tools.ListTools.first

class GitConfigurationSourceSpec extends Specification {

    def setup() {
		File tempContractFileDirectory = new File(GitConfigurationSource.DOWNLOAD_LOCATION)
		if(tempContractFileDirectory.exists()) {
			tempContractFileDirectory.deleteDir()
		}
	}

    def "contract files can be downloaded from a public git repo and unmarshalled into contract objects" () {
        given:
            GitConfigurationSource source = new GitConfigurationSource("https://bitbucket.org/harmingcola/contract-test-public.git")
        when:
            List<Contract> contracts = source.load()
            Contract contract = first(contracts)
        then:
            contracts.size() == 1
            contract.request.path == "/entity/1"
            contract.response.status == '200'
    }

    def "contract files can be downloaded from a private git repo and unmarshalled into contract objects" () {
        given:
            GitConfigurationSource source = new GitConfigurationSource("https://bitbucket.org/harmingcola/contract-test-private.git", 'seekay_test', 'seekay_test_password')
        when:
            List<Contract> contracts = source.load()
            Contract contract = first(contracts)
        then:
            contracts.size() == 1
            contract.request.path == "/entity/1"
            contract.response.status == '200'
    }

    def "the config source should delete an old source files before continuing" () {
        given:
            GitConfigurationSource oldConfigurationSource = new GitConfigurationSource("https://bitbucket.org/harmingcola/contract-test-public.git")
            oldConfigurationSource.load()
            GitConfigurationSource newConfigurationSource = new GitConfigurationSource("https://bitbucket.org/harmingcola/contract-test-public.git")
        when:
            List<Contract> contracts = newConfigurationSource.load()
            Contract contract = first(contracts)
        then:
            contracts.size() == 1
            contract.request.path == "/entity/1"
            contract.response.status == '200'
    }

    def "a problem during cloning the repository should be thrown as an illegal state" () {
        given:
            GitConfigurationSource source = Spy(GitConfigurationSource, constructorArgs:["https://bitbucket.org/harmingcola/contract-test-public.git"])
            1 * source.clonePublicRepository(_ as File) >> {throw new CheckoutConflictException("Its broken yo")}
        when:
            source.load()
        then:
            thrown(IllegalStateException)
    }

	def "contract files can be downloaded from our acceptance github repo and unmarshalled into contract objects" () {
		given:
            GitConfigurationSource source = new GitConfigurationSource("https://github.com/harmingcola/kvServerContracts.git")
		when:
			source.load()
		then:
			noExceptionThrown()
    }

    def 'a public repository checkout can be reused' () {
        given:
            def firstLoad = new GitConfigurationSource("https://bitbucket.org/harmingcola/contract-test-public.git").load()
        when:
            def secondLoad = new GitConfigurationSource("https://bitbucket.org/harmingcola/contract-test-public.git").load()
        then:
            noExceptionThrown()
            firstLoad == secondLoad
    }

    def 'a private repository checkout can be reused' () {
        given:
            def firstLoad = new GitConfigurationSource("https://bitbucket.org/harmingcola/contract-test-private.git", 'seekay_test', 'seekay_test_password').load()
        when:
            def secondLoad = new GitConfigurationSource("https://bitbucket.org/harmingcola/contract-test-private.git", 'seekay_test', 'seekay_test_password').load()
        then:
            noExceptionThrown()
            firstLoad == secondLoad
    }
}
