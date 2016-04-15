package org.seekay.contract.model.util

import org.apache.http.Header
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpDelete
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpPut
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import org.seekay.contract.model.domain.ContractResponse
import org.seekay.contract.model.domain.Method

import static org.seekay.contract.model.domain.Method.*


class Http {

    Method method
    String path
    String body
    Map<String, String> requestHeaders

    HttpClient httpClient
    HttpUriRequest request
    HttpResponse response

    private Http(Method method) {
        this.method = method;
        this.httpClient = HttpClientBuilder.create().build()
    }

    /*
     * Http Methods
     */

    static Http method(Method method) {
        switch (method) {
            case GET : get(); break;
            case POST: post(); break;
            case PUT: put(); break;
            case DELETE: delete(); break;
            default: throw new IllegalStateException("Unsupported method ["+ method +"] requested, supported are [GET, POST]")
        }
    }

    static Http get() {
        return new Http(GET);
    }

    static Http post() {
        return new Http(POST);
    }

    static Http put() {
        return new Http(PUT);
    }

    static Http delete() {
        return new Http(DELETE);
    }


    /*
     * Builders
     */

    Http toPath(String path) {
        this.path = path
        return this
    }

    Http fromPath(String path) {
        this.path = path
        return this
    }

    Http withBody(String body) {
        this.body = body
        return this;
    }

    Http withHeaders(Map<String, String> headers) {
        this.requestHeaders = headers
        return this;
    }

    /*
     * Build endpoint
     */

    Http execute() {
        if(method == GET) {
            request = new HttpGet(path)
        }
        if(method == POST) {
            HttpPost post = new HttpPost(path)
            post.setEntity(new StringEntity(body));
            request = post
        }
        if(method == PUT) {
            HttpPut put = new HttpPut(path)
            put.setEntity(new StringEntity(body));
            request = put
        }
        if(method == DELETE) {
            HttpDelete delete = new HttpDelete(path)
            request = delete
        }
        addHeaders()
        response = httpClient.execute(request)
        return this
    }

    /*
     * Return hooks
     */

    Integer getStatusCode() {
        return response.statusLine.statusCode
    }

    String getBody() {
        if(response.getEntity() != null) {
            return EntityUtils.toString(response.getEntity())
        }
        return null;
    }

    Map<String, String> getResponseHeaders() {
        Map<String, String> responseHeaders = new HashMap<String, String>();
        for(Header header : response.getAllHeaders()) {
            responseHeaders.put(header.name, header.value)
        }
        return responseHeaders;
    }

    ContractResponse toResponse() {
        return new ContractResponse(
                status: getStatusCode(),
                body: getBody(),
                headers: getResponseHeaders()
        )
    }


    /*
     * Private helpers
     */

    private def addHeaders() {
        if(requestHeaders != null && !requestHeaders.isEmpty()) {
            for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                request.addHeader(entry.key, entry.value);
            }
        }
    }
}
