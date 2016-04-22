def gitUrl = 'git@github.com:harmingcola/contract.git'
def branch = 'master'

job('contract_build') {
	quietPeriod(0)
	scm {
		git {
			branch(branch)
			remote {
				url(gitUrl)
			}

		}
	}
	steps {
		maven('clean install')
	}
	publishers {
		buildPipelineTrigger('kvClient_acceptance'){
			parameters {
				predefinedProp('CONTRACT_VERSION', '$CONTRACT_VERSION')
			}
		}
	}
	wrappers {
		preBuildCleanup()
	}
}

job('kvClient_acceptance') {
	quietPeriod(0)
	scm {
		git {
			branch(branch)
			remote {
				url('git@github.com:harmingcola/kvClient.git')
			}
		}
	}
	steps {
		maven('clean install -Dcontract.version=$CONTRACT_VERSION')
	}
	publishers {
		buildPipelineTrigger('kvServer_acceptance'){
			parameters {
				predefinedProp('CONTRACT_VERSION', '$CONTRACT_VERSION')
			}
		}
	}
	wrappers {
		preBuildCleanup()
	}
}

job('kvServer_acceptance') {
	quietPeriod(0)
	scm {
		git {
			branch(branch)
			remote {
				url('git@github.com:harmingcola/kvServer.git')
			}
		}
	}
	steps {
		shell('mvn spring-boot:run &')
		maven('clean install -Dcontract.version=$CONTRACT_VERSION')
	}
	publishers {
		buildPipelineTrigger('contract_release')
	}
	wrappers {
		preBuildCleanup()
	}
}

job('contract_release') {
	quietPeriod(0)
	scm {
		git {
			branch(branch)
			localBranch(branch)
			remote {
				url('https://github.com/harmingcola/contract.git')
			}
		}
	}
	steps {
		shell(readFileFromWorkspace('jenkins/src/main/resources/scripts/execute_release.sh'))
	}
	publishers {
		buildPipelineTrigger('contract_documentation'){
			parameters {
				predefinedProp('CONTRACT_VERSION', '$DEVELOPMENT_VERSION')
			}
		}
	}
	wrappers {
		preBuildCleanup()
	}
}

job('contract_documentation') {
	quietPeriod(0)
	scm {
		git {
			branch('master')
			remote {
				url('https://github.com/harmingcola/contract.git')
				credentials('seekay-jenkins-auth')
			}
		}
	}
	steps {
		shell(readFileFromWorkspace('jenkins/src/main/resources/scripts/documentation_update.sh'))
	}
	wrappers {
		preBuildCleanup()
	}
}

buildPipelineView('contract_pipeline') {
	selectedJob('contract_build')
	filterBuildQueue()
	displayedBuilds(4)
	refreshFrequency(60)
	showPipelineParameters()
	alwaysAllowManualTrigger(true)
}