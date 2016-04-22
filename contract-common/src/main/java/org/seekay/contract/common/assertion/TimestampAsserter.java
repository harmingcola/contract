package org.seekay.contract.common.assertion;

import org.seekay.contract.model.domain.ContractResponse;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.seekay.contract.common.enrich.enrichers.TimestampEnricher.*;

public class TimestampAsserter implements Asserter {

    private Pattern timestampWildcardPattern = Pattern.compile(TIMESTAMP_WILDCARD_REGEX);
    private Pattern fullBodyPattern = Pattern.compile(FULL_BODY_WILDCARD_REGEX);


    public void assertOnWildCards(ContractResponse contractResponse, ContractResponse actualResponse) {
        if(fullBodyPattern.matcher(contractResponse.getBody()).matches()) {
            Pattern pattern = buildPatternForRoughlyNow();
            String responseTimestamp = extractTimestamp(actualResponse.getBody(), pattern);
            updateContact(contractResponse, responseTimestamp);
        }
    }

    private Pattern buildPatternForRoughlyNow() {
        String timestamp = String.valueOf(new Date().getTime());
        timestamp = timestamp.substring(0,10);

        StringBuilder builder = new StringBuilder();
        builder.append("(.*)(");
        builder.append(timestamp);
        builder.append("([0-9]){3}");
        builder.append(")(.*)");

        return Pattern.compile(builder.toString());
    }

    private String extractTimestamp(String actualResponseBody, Pattern pattern) {
        Matcher matcher = pattern.matcher(actualResponseBody);
        if(matcher.matches()) {
            return matcher.group(0);
        }
        throw new AssertionError("Correct timestamp could not be found in response body");
    }


    private void updateContact(ContractResponse contractResponse, String responseTimestamp) {
        Matcher timestampWildcardMatcher = timestampWildcardPattern.matcher(contractResponse.getBody());
        String enrichedBody = timestampWildcardMatcher.replaceAll(responseTimestamp);
        contractResponse.setBody(enrichedBody);
    }
}
