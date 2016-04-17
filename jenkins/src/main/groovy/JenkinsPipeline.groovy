


job('contract_build') {
	scm {
		github('harmingcola/contract', 'master')
	}
	steps {
		maven('clean install')
	}
}