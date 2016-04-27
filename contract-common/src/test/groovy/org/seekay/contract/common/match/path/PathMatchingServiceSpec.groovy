package org.seekay.contract.common.match.path

import spock.lang.Specification

import static org.seekay.contract.model.ContractTestFixtures.defaultPostContract

class PathMatchingServiceSpec extends Specification {

    PathMatcher uglyPathMatcher = Mock(PathMatcher)
    PathMatcher prettyPathMatcher = Mock(PathMatcher)

    PathMatchingService service = new PathMatchingService(
        pathMatchers: [
                uglyPathMatcher, prettyPathMatcher
        ] as LinkedHashSet
    )

    def "for two paths, the matchers should be called"() {
        given:
            def contracts = [defaultPostContract().path('/index').build()] as Set
        when:
            def matches = service.findMatches(contracts, "/index")
        then:
            1 * uglyPathMatcher.isMatch(_ as String, _ as String) >> { return false }
            1 * prettyPathMatcher.isMatch(_ as String, _ as String) >> { return true }
            matches.size() == 1
    }
}
