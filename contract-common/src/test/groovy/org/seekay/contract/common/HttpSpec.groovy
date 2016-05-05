package org.seekay.contract.common
import org.apache.http.Header
import org.apache.http.HttpResponse
import org.apache.http.ProtocolVersion
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.*
import org.apache.http.entity.StringEntity
import org.apache.http.impl.DefaultHttpResponseFactory
import org.apache.http.util.EntityUtils
import org.seekay.contract.model.domain.Method
import spock.lang.Specification

class HttpSpec extends Specification {

	/*
	 * Request tests
	 */

	def "correct method should be set via builders ,get" () {
		given:
			def http = Http.method(Method.GET)
			def httpClient = Mock(HttpClient)
			http.httpClient = httpClient
		when:
			http.toPath("/hello/world").execute()
		then:
			httpClient.execute(_ as HttpUriRequest) >>  { request ->
				assert request[0] instanceof  HttpGet
			}
	}

	def "correct method should be set via builders, post" () {
		given:
			def http = Http.method(Method.POST)
			def httpClient = Mock(HttpClient)
			http.httpClient = httpClient
		when:
			http.toPath("/hello/world").withBody("if i have to").execute()
		then:
			httpClient.execute(_ as HttpUriRequest) >>  { request ->
				assert request[0] instanceof  HttpPost
			}
	}

	def "correct method should be set via builders, put" () {
		given:
			def http = Http.method(Method.PUT)
			def httpClient = Mock(HttpClient)
			http.httpClient = httpClient
		when:
			http.toPath("/hello/world").withBody("if i have to").execute()
		then:
			httpClient.execute(_ as HttpUriRequest) >>  { request ->
				assert request[0] instanceof HttpPut
			}
	}

	def "correct method should be set via builders, delete" () {
		given:
			def http = Http.method(Method.DELETE)
			def httpClient = Mock(HttpClient)
			http.httpClient = httpClient
		when:
			http.toPath("/hello/world").withBody("if i have to").execute()
		then:
			httpClient.execute(_ as HttpUriRequest) >>  { request ->
				assert request[0] instanceof HttpDelete
			}
	}

	def "a request should be made to the correct path" () {
		given:
			def http = Http.method(Method.POST)
			def httpClient = Mock(HttpClient)
			http.httpClient = httpClient
		when:
			http.toPath("/hello/world").withBody("if i have to").execute()
		then:
			httpClient.execute(_ as HttpUriRequest) >>  { request ->
				assert request[0].getURI().toString() == "/hello/world"
			}
	}

	def "a request should contain the correct body" () {
		given:
			def http = Http.method(Method.POST)
			def httpClient = Mock(HttpClient)
			http.httpClient = httpClient
		when:
			http.toPath("/hello/world").withBody("if i have to").execute()
		then:
			httpClient.execute(_ as HttpPost) >>  { request ->
				def entity =  request[0].getEntity()
				def body = EntityUtils.toString(entity)
				assert body == "if i have to"
			}
	}

	def "a request should contain the correct headers" () {
		given:
			def http = Http.method(Method.POST)
			def httpClient = Mock(HttpClient)
			http.httpClient = httpClient
		when:
			http.toPath("/hello/world").withBody("if i have to").withHeaders(["key":"value"]).execute()
		then:
			httpClient.execute(_ as HttpUriRequest) >>  { request ->
				HttpPost postRequest =  request[0]
				Header[] headers = postRequest.getAllHeaders()
				assert headers[0].getName() == "key"
				assert headers[0].getValue() == "value"
			}
	}

	def "requests in unsupported methods should throw an illegal state" () {
		when:
			Http.method(Method.CONNECT)
		then:
			IllegalStateException e = thrown()
			e.message.startsWith("Unsupported method CONNECT requested, supported are")

	}


	/*
	 * Response tests
	 */

	def "the correct response code should be returned" () {
		given:
			def http = Http.method(Method.GET)
			def httpClient = Mock(HttpClient)
			http.httpClient = httpClient
		when:
			def contractResponse = http.toPath("/hello/world").execute().toResponse()
		then:
			httpClient.execute(_ as HttpUriRequest) >> httpResponse(200, null)
			contractResponse.status == 200
	}

	def "the correct body should be returned" () {
		given:
			def http = Http.method(Method.GET)
			def httpClient = Mock(HttpClient)
			http.httpClient = httpClient
		when:
			def contractResponse = http.fromPath("/hello/world").withBody("hi there").execute().toResponse()
		then:
			httpClient.execute(_ as HttpUriRequest) >> httpResponse(204, "boom goes the server")
			contractResponse.body == "boom goes the server"
		}

	HttpResponse httpResponse(int statusCode, String body) {
		def factory = new DefaultHttpResponseFactory()
		def protocolVersion = new ProtocolVersion("http", 1,2)
		def response = factory.newHttpResponse(protocolVersion, statusCode, null)
		if(body != null) {
			response.setEntity(new StringEntity(body))
		}
		return response
	}
}
