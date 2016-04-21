package org.seekay.contract.common.match.body

import spock.lang.Specification

import static org.seekay.contract.model.ContractTestFixtures.oneDefaultContractOfEachMethod

class BodyMatcherServiceSpec extends Specification {

    BodyMatcher uglyBodyMatcher = Mock(BodyMatcher)
    BodyMatcher prettyBodyMatcher = Mock(BodyMatcher)

    BodyMatchService service = new BodyMatchService(
            bodyMatchers: [
                    uglyBodyMatcher, prettyBodyMatcher
            ] as Set
    )

    def 'a findMatches call will be forwarded to every body matcher implementation' () {
        when:
            service.findMatches(oneDefaultContractOfEachMethod(), "Hello World")
        then:
            1 * uglyBodyMatcher.findMatches(_,_) >> {return []}
            1 * prettyBodyMatcher.findMatches(_,_) >> {return []}
    }

    def 'an isMatch call will be forwarded to every body matcher implementation and if found, return true' () {
        when:
            def matchFound = service.isMatch("hello world", "Hello World")
        then:
            1 * uglyBodyMatcher.isMatch(_ as String,_ as String) >> {return false}
            1 * prettyBodyMatcher.isMatch(_ as String,_ as String) >> {return true}
            matchFound
    }

    def 'an isMatch call will be forwarded to every body matcher implementation and if found, return false' () {
        when:
            def matchFound = service.isMatch("hello world", "Hello World")
        then:
            1 * uglyBodyMatcher.isMatch(_ as String,_ as String) >> {return false}
            1 * prettyBodyMatcher.isMatch(_ as String,_ as String) >> {return false}
            !matchFound
    }
}
