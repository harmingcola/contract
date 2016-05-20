package org.seekay.contract.common.variable

import spock.lang.Specification

class StringVariableExtractorSpec extends Specification {

    StringVariableExtractor extractor = new StringVariableExtractor()

    def 'a variable should be extracted from a string with nothing but a variable expression' () {
        given:
            def contractString = '${contract.var.repoId}'
            def actualString = '45'
        when:
            def result = extractor.extract(contractString, actualString)
        then:
            result['repoId'] == '45'
    }

    def 'multiple variables should be extracted from a string with multiple variable expressions' () {
        given:
            def contractString = 'GIT DETAILS, ${contract.var.repoId},${contract.var.commitHash}, ${contract.var.commitCount}'
            def actualString = 'GIT DETAILS, 46,d683ed502d286ae39c988b9f6228a514349517e1, 163'
        when:
            def result = extractor.extract(contractString, actualString)
        then:
            result['repoId'] == '46'
            result['commitHash'] == 'd683ed502d286ae39c988b9f6228a514349517e1'
            result['commitCount'] == '163'
    }
}
