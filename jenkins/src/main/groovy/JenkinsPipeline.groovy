


job('contract_build') {
	scm {
		git {
			branch('master')
			remote {
				credentials('git-hub-harmingcola-auth')
				github('harmingcola/contract')
			}
		}
	}
	steps {
		maven('clean install')
	}
}