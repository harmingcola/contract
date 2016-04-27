package org.seekay.contract.model.tools;


import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class HeaderTools {

    /**
     * Private constructor for utility class
     */
    private HeaderTools() {
        throw new IllegalStateException("Utility classes should never be constructed");
    }

    public static Map<String, String> extractHeaders(HttpServletRequest request) {
        Map<String, String> result = new HashMap<String, String>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            result.put(headerName, request.getHeader(headerName));
        }
        return result;
    }

    public static boolean isSubMap(Map<String, String> subMap, Map<String, String> superMap) {
        if(subMap == null || subMap.isEmpty()) {
            return true;
        }
        for(Map.Entry<String, String> subMapEntry : subMap.entrySet()) {
            if(!containsEntry(subMapEntry, superMap)) {
                return false;
            }
        }
        return true;
    }

    public static boolean containsEntry(Map.Entry<String, String> entry, Map<String, String> map) {
        return map.containsKey(entry.getKey()) && map.get(entry.getKey()).equals(entry.getValue());
    }
}
