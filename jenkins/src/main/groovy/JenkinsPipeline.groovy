job('contract_build') {
	quietPeriod(0)
	wrappers {
		preBuildCleanup()
		configure { wrappers ->
			wrappers << 'com.lookout.jenkins.EnvironmentScript' (plugin: 'environment-script@1.2.2') {
				script 'echo CONTRACT_VERSION=$(xmlstarlet sel -N x="http://maven.apache.org/POM/4.0.0" -t -v \'/x:project/x:version\' pom.xml)'
				scriptType 'unixScript'
				onlyRunOnParent 'false'
			}
		}
	}
	scm {
		git {
			branch('master')
			remote {
				url('git@github.com:harmingcola/contract.git')
			}
		}
	}
	steps {
		maven('clean install')
		maven('sonar:sonar')
	}
	publishers {
		downstreamParameterized {
			trigger('kvClient_acceptance') {
				parameters {
					predefinedProp('CONTRACT_VERSION', '$CONTRACT_VERSION')
				}
			}
		}
	}
}

job('kvClient_acceptance') {
	quietPeriod(0)
	wrappers {
		preBuildCleanup()
	}
	scm {
		git {
			branch('master')
			remote {
				url('git@github.com:harmingcola/kvClient.git')
			}
		}
	}
	steps {
		maven('clean install -Dcontract.version=$CONTRACT_VERSION')
	}
	publishers {
		downstreamParameterized {
			trigger('kvServer_acceptance') {
				parameters {
					predefinedProp('CONTRACT_VERSION', '$CONTRACT_VERSION')
				}
			}
		}
	}
}

job('kvServer_acceptance') {
	quietPeriod(0)
	wrappers {
		preBuildCleanup()
	}
	scm {
		git {
			branch('master')
			remote {
				url('git@github.com:harmingcola/kvServer.git')
			}
		}
	}
	steps {
		shell('mvn spring-boot:run -Dserver.port=8090  &')
		shell('echo Waiting 20 seconds for kvServer to start')
		shell('sleep 20')
		maven('clean install -Dcontract.version=$CONTRACT_VERSION')
	}
	publishers {
		buildPipelineTrigger('contract_release')
	}
}

job('contract_release') {
	quietPeriod(0)
	wrappers {
		preBuildCleanup()
	}
	scm {
		git {
			branch('master')
			localBranch('master')
			remote {
				url('git@github.com:harmingcola/contract.git')
			}
		}
	}
	steps {
		shell(readFileFromWorkspace('jenkins/src/main/resources/scripts/execute_release.sh'))
	}
	publishers {
		downstreamParameterized {
			trigger('contract_documentation') {
				parameters {
					predefinedProp('CONTRACT_VERSION', '$CONTRACT_VERSION')
				}
			}
		}
	}
}

job('contract_documentation') {
	quietPeriod(0)
	wrappers {
		preBuildCleanup()
	}
	scm {
		git {
			branch('master')
			remote {
				url('git@github.com:harmingcola/contract.git')
			}
		}
	}
	steps {
		shell(readFileFromWorkspace('jenkins/src/main/resources/scripts/documentation_update.sh'))
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