package org.seekay.contract.common.enrich.enrichers;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimestampEnricher implements Enricher {

    public static final String TIMESTAMP_WILDCARD_REGEX = "\\$\\{contract.timestamp\\}";
    public static final String FULL_BODY_WILDCARD_REGEX = "(.*)(\\$\\{contract.timestamp\\})(.*)";

    private Pattern timestampWildcardPattern = Pattern.compile(TIMESTAMP_WILDCARD_REGEX);
    private Pattern fullBodyWildcardPattern = Pattern.compile(FULL_BODY_WILDCARD_REGEX);

    public String enrichResponseBody(String body) {
        if(body == null) {
            return body;
        }
        if(fullBodyWildcardPattern.matcher(body).matches()) {
            String timestamp = String.valueOf(new Date().getTime());
            Matcher timestampMatcher = timestampWildcardPattern.matcher(body);
            String result = timestampMatcher.replaceAll(timestamp);
            return result;
        } else {
            return body;
        }
    }
}
