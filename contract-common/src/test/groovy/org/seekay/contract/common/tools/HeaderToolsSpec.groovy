package org.seekay.contract.common.tools

import spock.lang.Specification

import javax.servlet.http.HttpServletRequest


class HeaderToolsSpec extends Specification {

    def "HeaderTools should never be constructed" () {
        when:
        	HeaderTools.class.newInstance()
        then:
			IllegalStateException e = thrown()
			e.message == "Utility classes should never be constructed"
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
}
