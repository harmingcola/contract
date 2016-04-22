package org.seekay.contract.server.util;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class ResponseWriter {

    private HttpServletResponse response;

    private ResponseWriter(HttpServletResponse response) {
        this.response = response;
    }

    public static ResponseWriter to(HttpServletResponse response) {
        return new ResponseWriter(response);
    }

    public ResponseWriter headers(Map<String, String> headers) {
        if(headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                response.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    public void write(String body) {
        try {
            ServletOutputStream out = response.getOutputStream();
            if(body != null) {
                out.write(body.getBytes()); //NOSONAR
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new IllegalStateException("Problem occurred in writing response", e);
        }
    }

    public ResponseWriter ok() {
        response.setStatus(200); return this;
    }

    public ResponseWriter created() {
        response.setStatus(201); return this;
    }

    public ResponseWriter notFound() {
        response.setStatus(404); return this;
    }

    public ResponseWriter status(Integer status) {
        response.setStatus(status); return this;
    }
}
