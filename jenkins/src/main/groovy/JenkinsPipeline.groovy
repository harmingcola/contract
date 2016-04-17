


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