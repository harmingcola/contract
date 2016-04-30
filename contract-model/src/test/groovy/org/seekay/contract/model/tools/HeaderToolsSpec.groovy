package org.seekay.contract.model.tools

import spock.lang.Specification

import javax.servlet.http.HttpServletRequest


class HeaderToolsSpec extends Specification {

    def "header tools should never be constructed" () {
        when:
        	HeaderTools.class.newInstance()
        then:
			IllegalStateException e = thrown()
			e.message == "Utility classes should never be constructed"
    }

    def "a null map should be a subset of a map" () {
        given:
            def subMap = null
            def superMap = aPopulatedMap()
        when:
            boolean result = HeaderTools.isSubMap(subMap, superMap)
        then:
            result
    }

    def "an empty map should be a subset of a map" () {
        given:
            def subMap = new HashMap<String,String>()
            def superMap = aPopulatedMap()
        when:
            boolean result = HeaderTools.isSubMap(subMap, superMap)
        then:
            result
    }

    def "an identical map should be a subset of a map" () {
        given:
            def subMap = aPopulatedMap()
            def superMap = aPopulatedMap()
        when:
            boolean result = HeaderTools.isSubMap(subMap, superMap)
        then:
            result
    }

    def "an map missing a single field should be a subset of a map" () {
        given:
            def subMap = aPopulatedMap()
            subMap.remove("Content-Type:")
            def superMap = aPopulatedMap()
        when:
            boolean result = HeaderTools.isSubMap(subMap, superMap)
        then:
            result
    }

    def "a map with a single additional field is not a subMap" () {
        given:
            def subMap = aPopulatedMap()
            subMap.put("status","grand")
            def superMap = aPopulatedMap()
        when:
            boolean result = HeaderTools.isSubMap(subMap, superMap)
        then:
            !result
    }

    def "a map with a multiple additional fields is not a subMap" () {
        given:
            def subMap = aPopulatedMap()
            subMap.put("status","grand")
            subMap.put("superhero","batman")
            def superMap = aPopulatedMap()
        when:
            boolean result = HeaderTools.isSubMap(subMap, superMap)
        then:
            !result
    }

	def "headers should be extracted correctly" () {
		given:
			Enumeration<String> headerNames = new StringTokenizer("browser time")
			HttpServletRequest request = Mock(HttpServletRequest)
		when:
			Map result = HeaderTools.extractHeaders(request)
		then:
			result["browser"] == "chrome"
			result["time"] == "nowish"
			1 * request.getHeaderNames() >> headerNames
			1 * request.getHeader("browser") >> "chrome"
			1 * request.getHeader("time") >> "nowish"

	}

    Map<String,String> aPopulatedMap() {
        return [
                "Content-Type:":" application/json",
                "Server": "Apache something or other"
        ]
    }
}
