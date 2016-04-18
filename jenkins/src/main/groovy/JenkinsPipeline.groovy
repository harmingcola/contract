job('contract_build') {
	scm {
		git {
			branch('master')
			remote {
				credentials('github-harmingcola-auth')
				github('harmingcola/contract')
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
	scm {
		git {
			branch('master')
			localBranch('master')
			remote {
				credentials('github-harmingcola-auth')
				github('harmingcola/contract')
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
	scm {
		git {
			branch('master')
			remote {
				credentials('github-harmingcola-auth')
				github('harmingcola/contract')
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

buildPipelineView('pipeline') {
	selectedJob('contract_build')
	filterBuildQueue()
	displayedBuilds(4)
	refreshFrequency(60)
	showPipelineParameters()
	alwaysAllowManualTrigger(true)
}