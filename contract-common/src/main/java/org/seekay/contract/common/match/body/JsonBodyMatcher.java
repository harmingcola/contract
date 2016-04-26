package org.seekay.contract.common.match.body;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.seekay.contract.common.ApplicationContext.*;

@Slf4j
public class JsonBodyMatcher implements BodyMatcher {

    private ObjectMapper objectMapper = objectMapper();

    public boolean isMatch(String contractBody, String actualBody) {
        try {
            Object contractObject = objectMapper.readValue(contractBody, Object.class);
            Object actualObject = objectMapper.readValue(actualBody, Object.class);
            return doObjectsMatch(contractObject, actualObject);
        } catch (IOException e) {
            log.info("An error occurred reading json response");
            return true;
        }
    }

    private boolean doObjectsMatch(Object contractObject, Object actualObject) {
        if(contractObject == null && actualObject == null) {
            return true;
        } else if(contractObject == null || actualObject == null) {
            return false;
        } else if(contractObject.getClass() != actualObject.getClass()) {
            return false;
        } else if(contractObject instanceof Map) {
            return doMapsMatch((Map<String, Object>) contractObject, (Map<String, Object>) actualObject);
        } else if(contractObject instanceof Number) {
            return doNumbersMatch((Number) contractObject, (Number) actualObject);
        } else if(contractObject instanceof String) {
            return doStringsMatch((String) contractObject, (String) actualObject);
        } else if(contractObject instanceof Boolean) {
            return doBooleansMatch((Boolean) contractObject, (Boolean) actualObject);
        } else if(contractObject instanceof List) {
            return doListsMatch((List) contractObject, (List) actualObject);
        }
        return true;
    }

    private boolean doListsMatch(List contractList, List actualList) {
        if(contractList.size() != actualList.size()) {
            return false;
        }
        int matchedCount = 0;
        for(Object contractObject : contractList) {
            for(Object actualObject : actualList) {
                if(contractObject.hashCode() == actualObject.hashCode()) {
                    if(!doObjectsMatch(contractObject, actualObject)) {
                        return false;
                    } else {
                        matchedCount++;
                    }
                }
            }
        }
        if(matchedCount != contractList.size()) {
            return false;
        }
        return true;
    }

    private boolean doBooleansMatch(Boolean contactBoolean, Boolean actualBoolean) {
        return contactBoolean.equals(actualBoolean);
    }

    private boolean doStringsMatch(String contractString, String actualString) {
        return contractString.equals(actualString);
    }

    private boolean doNumbersMatch(Number contractNumber, Number actualNumber) {
        return contractNumber.equals(actualNumber);
    }

    private boolean doMapsMatch(Map<String, Object> contractMap, Map<String, Object> actualMap) {
        Set<String> keysChecked = new HashSet<String>();
        for(String key : contractMap.keySet()) {
            keysChecked.add(key);
            Object contractObject = contractMap.get(key);
            Object actualObject = actualMap.get(key);
            if(!doObjectsMatch(contractObject, actualObject)) {
                return false;
            }
        }
        if(!keysChecked.containsAll(actualMap.keySet())) {
            return false;
        }
        return true;
    }
}
