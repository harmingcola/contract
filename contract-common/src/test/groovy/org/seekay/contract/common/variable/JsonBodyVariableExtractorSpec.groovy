package org.seekay.contract.common.variable

import com.fasterxml.jackson.databind.ObjectMapper
import org.seekay.contract.common.match.body.JsonBodyMatcher
import spock.lang.Specification

class JsonBodyVariableExtractorSpec extends Specification {

    JsonBodyVariableExtractor extractor =  new JsonBodyVariableExtractor()

    ObjectMapper objectMapper = new ObjectMapper()
    StringVariableExtractor stringVariableExtractor = new StringVariableExtractor()
    JsonBodyMatcher jsonBodyMatcher = new JsonBodyMatcher()

    def setup() {
        extractor.objectMapper = objectMapper
        extractor.stringVariableExtractor = stringVariableExtractor
        extractor.jsonBodyMatcher = jsonBodyMatcher
    }

    def 'failure from acceptance 001' () {
        given:
            def contractString = '{"id":"${contract.var.string.repoId}","name":"kvServerContracts","url":"https://github.com/harmingcola/kvServerContracts.git"}'
            def actualString = '{"id":"b6697967-db3a-4ce9-a7bd-ac86c1da9dfc","url":"https://github.com/harmingcola/kvServerContracts.git","name":"kvServerContracts","username":null,"password":null}'
        when:
            def result = extractor.extract(contractString, actualString)
        then:
            result['repoId'] == 'b6697967-db3a-4ce9-a7bd-ac86c1da9dfc'
    }
}
