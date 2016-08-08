package org.seekay.contract.common

import spock.lang.Specification

class HttpTestTest extends Specification {

    def 'headers check' () {
        expect:
            Http.put().toPath("http://localhost:9050/ct/services/repositories/R001")
                    .withHeaders(['content-type':'application/json', 'Origin':'*'])
                    .withBody('{"id":"R001","url":"https://github.com/harmingcola/kvServerContracts.git","name":"kvServer Contracts","username":"Dave","password":"Marvin"}')
                    .execute().getResponseHeaders() != null
    }
}
