package org.seekay.contract.common.match.body

import org.seekay.contract.common.match.common.ExpressionMatcher
import spock.lang.Specification

class ExpressionBodyMatcherSpec extends Specification {

    def 'a call to the expression body matcher should be forwarded to the expression matcher' () {
        given:
            ExpressionMatcher expressionMatcher = Mock(ExpressionMatcher)
            ExpressionBodyMatcher expressionBodyMatcher = new ExpressionBodyMatcher(
                    expressionMatcher: expressionMatcher
            )
        when:
            expressionBodyMatcher.isMatch('/hello', '/hello')
        then:
            1 * expressionMatcher.isMatch(_ as String, _ as String) >> {return true}
    }
}
