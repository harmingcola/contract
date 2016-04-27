package org.seekay.contract.model.tools

import spock.lang.Specification

import static org.seekay.contract.model.tools.SetTools.*


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

    def "a set converted to an array should have all elements"() {
        given:
            def set = ["zero", "one", "two", "three", "four"] as LinkedHashSet<String>
        when:
            def array = toArray(set)
        then:
            array[0] == "zero"
            array[1] == "one"
            array[2] == "two"
            array[3] == "three"
            array[4] == "four"
    }

    def "an empty set converted to an array should have no elements"() {
        given:
            def set = [] as LinkedHashSet<String>
        when:
            def array = toArray(set)
        then:
            array.length == 0
    }

    def "a null set converted to an array should have no elements"() {
        given:
            def set = null
        when:
            def array = toArray(set)
        then:
            array.length == 0
    }

    def "an array converted to a set should have all elements"() {
        given:
            String[] array = "zero one two three four".split()
        when:
            def set = SetTools.toSet(array)
        then:
            set.contains("zero")
            set.contains("one")
            set.contains("two")
            set.contains("three")
            set.contains("four")
    }

    def "an empty array converted to a set should have no elements"() {
        given:
            def array = new String[0]
        when:
            def set = toSet(array)
        then:
            set.size() == 0
    }

    def "a null array converted to a set should have no elements"() {
        given:
            def array = null
        when:
            def set = toSet(array)
        then:
            set.size() == 0
    }
}
