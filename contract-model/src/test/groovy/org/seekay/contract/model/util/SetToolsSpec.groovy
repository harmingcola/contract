package org.seekay.contract.model.util

import spock.lang.Specification

import static org.seekay.contract.model.util.SetTools.*


class SetToolsSpec extends Specification {

    def "set tools should never be constructed" () {
        when:
        	SetTools.class.newInstance()
        then:
        	IllegalStateException e = thrown()
        	e.message == "Utility classes should never be constructed"
    }

    def "3 sets with 1 matching element in each should return 1 element" () {
        when:
            def result = intersectingElements([1] as Set, [1] as Set, [1] as Set)
        then:
            result.size() == 1
            head(result) == 1
    }

    def "3 sets with 0 matching elements should return no elements" () {
        when:
            def result = intersectingElements([1] as Set, [2] as Set, [3] as Set)
        then:
            result.size() == 0
    }

    def "3 sets with matching elements in only 2 sets should return no elements" () {
        when:
            def result = intersectingElements([1] as Set, [1] as Set, [2] as Set)
        then:
            result.size() == 0
    }

    def "4 sets, 2 elements in each, 1 matching element, should return 1 element" () {
        when:
        def result = intersectingElements([1,2] as Set, [1,3] as Set, [1,4] as Set, [1,5] as Set)
        then:
            result.size() == 1
            head(result) == 1
    }

    def "3 sets, 3 elements in each, 2 matching elements, should return 2 elements" () {
        when:
            def result = intersectingElements([1,2,3] as Set, [1,2,4] as Set, [1,2,5] as Set)
        then:
            result.size() == 2
            head(result) == 1
            head(tail(result)) == 2
    }

    def "4 sets, varying elements in each, 1 matching element, should return 1 element" () {
        when:
            def result = intersectingElements([1,2,3,4,5] as Set, [1] as Set, [1,2] as Set, [1,6,7,8,9] as Set)
        then:
            result.size() == 1
            head(result) == 1
    }
}
