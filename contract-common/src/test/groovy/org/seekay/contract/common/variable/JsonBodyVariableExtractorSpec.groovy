package org.seekay.contract.common.variable

import com.fasterxml.jackson.databind.ObjectMapper
import org.seekay.contract.common.match.body.JsonBodyMatcher
import org.seekay.contract.common.match.common.ExpressionMatcher
import spock.lang.Specification

class JsonBodyVariableExtractorSpec extends Specification {

    JsonBodyVariableExtractor extractor =  new JsonBodyVariableExtractor()

    def setup() {
        extractor.objectMapper = new ObjectMapper()
        extractor.stringVariableExtractor = new StringVariableExtractor()
        extractor.jsonBodyMatcher = new JsonBodyMatcher(
            expressionMatcher: new ExpressionMatcher()
        )
    }

    def 'both objects being null should be handled gracefully' () {
        when:
            def result = extractor.extract(null, null);
        then:
            result.size() == 0
    }

    def 'if the contract object is null should be handled gracefully' () {
        when:
            def result = extractor.extract(null, "{}");
        then:
            result.size() == 0
    }

    def 'if the actual object is null should be handled gracefully' () {
        when:
            def result = extractor.extract("{}", null);
        then:
            result.size() == 0
    }

    def 'if json objects are different types, but a variable is used, it should be extracted' () {
        given:
            def contractString = '{"id":"${contract.var.string.repoId}"}'
            def actualString = '{"id":697967}'
        when:
            def result = extractor.extract(contractString, actualString)
        then:
            result['repoId'] == "697967"
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

    def 'exceptions thrown during json parsing should be handled gracefully' () {
        given:
            JsonBodyVariableExtractor extractor = new JsonBodyVariableExtractor()
            ObjectMapper objectMapper = Mock(ObjectMapper)
            extractor.objectMapper = objectMapper
        when:
            def result = extractor.extract("","")
        then:
            result.size() == 0
            1 * objectMapper.readValue(_ as String, Object.class) >> {throw new IOException()}
    }

    def 'variables should be extracted from lists correctly' () {
        given:
            def contractString = '[{"id":"${contract.var.string.repo.id.1}"}, {"id":"${contract.var.string.repo.id.2}"}]'
            def actualString = '[{"id":"whiskey"}, {"id":"foxtrot"}]'
        when:
            def result = extractor.extract(contractString, actualString)
        then:
            result['repo.id.1'] == 'whiskey'
            result['repo.id.2'] == 'foxtrot'
    }
}
