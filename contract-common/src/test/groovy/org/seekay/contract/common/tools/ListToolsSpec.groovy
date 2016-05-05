package org.seekay.contract.common.tools

import spock.lang.Specification


class ListToolsSpec extends Specification {

    def "ListTools should never be constructed" () {
        when:
            ListTools.class.newInstance()
        then:
            IllegalStateException e = thrown()
            e.message == "Utility classes should never be constructed"
    }

    def "the first method should return the first element of a list" () {
        given:
            def list = [1,2,3,4]
        when:
            def result = ListTools.first(list)
        then:
            result == 1
    }

    def "a list with no elements should return null" () {
        given:
            def list = []
        when:
            def result = ListTools.first(list)
        then:
            result == null
    }

}
