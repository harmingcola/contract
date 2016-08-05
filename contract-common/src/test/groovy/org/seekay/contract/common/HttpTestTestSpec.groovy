package org.seekay.contract.common

import spock.lang.Specification

class HttpTestTestSpec extends Specification {

    def 'headers check' () {
        expect:
            Http.get().fromPath("http://localhost:9050/ct/services/repositories").execute().getResponseHeaders() != null
    }
}
