package org.seekay.contract.server.util;

import com.google.common.io.CharStreams;
import org.seekay.contract.model.domain.ContractRequest;
import org.seekay.contract.model.domain.Method;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.seekay.contract.model.tools.HeaderTools.extractHeaders;

public class RequestReader {

    private HttpServletRequest request;

    public RequestReader(HttpServletRequest request) {
        this.request = request;
    }

    public static RequestReader from (HttpServletRequest request) {
        return new RequestReader(request);
    }

    public String readBody() {
        String result;
        try {
            result = CharStreams.toString(this.request.getReader());
        } catch (IOException e) {
            throw new IllegalStateException("Problem occurred during conversion of servlet request to contract request", e);
        }
        return result;
    }

    public ContractRequest toContractRequest() {
        ContractRequest contractRequest = new ContractRequest();
        contractRequest.setMethod(Method.valueOf(request.getMethod()));
        contractRequest.setPath(buildPath(request));
        contractRequest.setHeaders(extractHeaders(request));
		    contractRequest.setBody(readBody());
        return contractRequest;
    }

    private String buildPath(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder(request.getPathInfo());
        if(request.getQueryString() != null) {
            builder.append("?");
            builder.append(request.getQueryString());
        }
        return builder.toString();
    }
}
