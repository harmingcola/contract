package org.seekay.contract.common.matchers
import org.seekay.contract.common.match.path.ExactPathMatcher
import spock.lang.Shared
import spock.lang.Specification

class ExactPathMatcherSpec extends Specification {

    @Shared
    ExactPathMatcher matcher = new ExactPathMatcher()

    def 'two exactly matching paths should match' () {
        expect:
            matcher.isMatch("/index", "/index")
    }

    def 'two different paths should match' () {
        expect:
            !matcher.isMatch("/index", "/welcome")
    }
}
