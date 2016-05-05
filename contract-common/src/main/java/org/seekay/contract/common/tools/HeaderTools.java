package org.seekay.contract.common.tools;


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
}
