package org.seekay.contract.common.variable

import spock.lang.Specification

class StringVariableExtractorSpec extends Specification {

    StringVariableExtractor extractor = new StringVariableExtractor()

    def 'a variable should be extracted from a string with nothing but a variable expression' () {
        given:
            def contractString = '${contract.var.number.repoId}'
            def actualString = '45'
        when:
            def result = extractor.extract(contractString, actualString)
        then:
            result['repoId'] == '45'
    }

    def 'multiple variables should be extracted from a string with multiple variable expressions' () {
        given:
            def contractString = 'GIT DETAILS, ${contract.var.number.repoId},${contract.var.number.commitHash}, ${contract.var.number.commitCount}'
            def actualString = 'GIT DETAILS, 46,6835028639988962285143495171, 163'
        when:
            def result = extractor.extract(contractString, actualString)
        then:
            result['repoId'] == '46'
            result['commitHash'] == '6835028639988962285143495171'
            result['commitCount'] == '163'
    }

    def 'Errors when processing a json list should be handled gracefully'() {
        given:
            def contractString = '[{"id":"${contract.var.string.repoId}","name":"kvServerContracts","url":"https://github.com/harmingcola/kvServerContracts.git"}]'
            def actualString = '[{"id":"8feb069d-a68e-4a2d-8f8d-cc01925988e2","url":"https://github.com/harmingcola/kvServerContracts.git","name":"kvServerContracts","username":null,"password":null}]'
        when:
            extractor.extract(contractString, actualString)
        then:
            notThrown(IndexOutOfBoundsException)
    }
}
