package org.seekay.contract.server.match
import org.seekay.contract.model.domain.Contract
import spock.lang.Shared
import spock.lang.Specification

import static org.seekay.contract.model.ContractTestFixtures.defaultGetContract
import static org.seekay.contract.model.ContractTestFixtures.oneDefaultContractOfEachMethodWithoutHeaders
import static org.seekay.contract.model.util.SetTools.head

class HeaderMatcherSpec extends Specification {

    @Shared
    HeaderMatcher matcher = new HeaderMatcher()

    def "a single request should be matched on its headers" () {
        given:
            Set<Contract> contracts = [defaultGetContract().build()]
        when:
            Set result = matcher.match(contracts, ["captain":"america"])
        then:
            result.size() == 1
            head(result).request.headers["captain"] == "america"
    }

    def "a contract without request headers will match every http request" () {
        given:
            Set contracts = oneDefaultContractOfEachMethodWithoutHeaders()
        when:
            Set result = matcher.match(contracts, ["captain":"america"])
        then:
            result.size() == contracts.size()
    }

    def "matcher can match several contracts" () {
        given:
            Set contracts = [
                    defaultGetContract().path("/entity/1").requestHeaders(["iron":"man"]).responseBody("not filtered").build(),
                    defaultGetContract().path("/entity/2").requestHeaders(["iron":"man"]).responseBody("not filtered").build(),
                    defaultGetContract().path("/entity/3").requestHeaders(["steve":"rogers"]).responseBody("filtered").build()
            ]
        when:
            Set result = matcher.match(contracts, ["iron":"man"])
        then:
            result.size() == 2
            result.collect { contract ->
                assert contract.response.body == "not filtered"
            }
    }
}
