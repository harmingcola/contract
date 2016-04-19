job('contract_build') {
	quietPeriod(0)
	scm {
		git {
			branch('master')
			remote {
				url('git@github.com:harmingcola/contract.git')
				credentials('7d996866-e66a-4d5e-96e5-b881045e96b8')
			}

		}
	}
	steps {
		maven('clean install')
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
			branch('master')
			localBranch('master')
			remote {
				url('git@github.com:harmingcola/contract.git')
				credentials('7d996866-e66a-4d5e-96e5-b881045e96b8')
			}
		}
	}
	steps {
		shell(readFileFromWorkspace('jenkins/src/main/resources/scripts/execute_release.sh'))
	}
	publishers {
		buildPipelineTrigger('contract_documentation')
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
				url('git@github.com:harmingcola/contract.git')
				credentials('7d996866-e66a-4d5e-96e5-b881045e96b8')
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