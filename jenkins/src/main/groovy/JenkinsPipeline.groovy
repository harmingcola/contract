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
}

job('contract_release') {
	scm {
		git {
			branch('master')
			localbranch('master')
			remote {
				credentials('github-harmingcola-auth')
				github('harmingcola/contract')
			}
		}
	}
	steps {
		shell(readFileFromWorkspace('jenkins/src/main/resources/scripts/execute_release.sh'))
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
}