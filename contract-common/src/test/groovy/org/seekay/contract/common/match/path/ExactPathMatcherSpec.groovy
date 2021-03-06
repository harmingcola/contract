package org.seekay.contract.common.match.path

import spock.lang.Shared
import spock.lang.Specification

class ExactPathMatcherSpec extends Specification {

    @Shared
    ExactPathMatcher matcher = new ExactPathMatcher()

    def 'two exactly matching paths should match' () {
        expect:
            matcher.isMatch("/index", "/index")
    }

    def 'two exactly matching paths, one with a slash should match, A' () {
        expect:
            matcher.isMatch("/index/", "/index")
    }

    def 'two exactly matching paths, one with a slash should match, B' () {
        expect:
            matcher.isMatch("/index", "/index/")
    }

    def 'two different paths should match' () {
        expect:
            !matcher.isMatch("/index", "/welcome")
    }
}
