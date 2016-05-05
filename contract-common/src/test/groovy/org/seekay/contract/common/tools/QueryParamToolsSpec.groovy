package org.seekay.contract.common.tools

import spock.lang.Specification

class QueryParamToolsSpec extends Specification {

    def "QueryParamTools should never be constructed" () {
        when:
            QueryParamTools.class.newInstance()
        then:
            IllegalStateException e = thrown()
            e.message == "Utility classes should never be constructed"
    }

    def 'a path with no params should return an empty map' () {
        when:
            def result = QueryParamTools.extractParameters("/index")
        then:
            result.size() == 0
    }

    def 'a path with a single param should have both key and value extracted correctly' () {
        when:
            def result = QueryParamTools.extractParameters("/index?result=success")
        then:
            result.size() == 1
            result['result'] == 'success'
    }

    def 'a path with multiple params should have both keys and values extracted correctly' () {
        when:
            def result = QueryParamTools.extractParameters("/index?result=success&name=dave&count=1")
        then:
            result.size() == 3
            result['result'] == 'success'
            result['name'] == 'dave'
            result['count'] == '1'
    }
}
