package org.seekay.contract.model.util
import com.fasterxml.jackson.core.JsonGenerationException
import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

class PrintToolsSpec extends Specification {

	ObjectMapper objectMapper = new ObjectMapper();

	def "print tools should never be constructed" () {
		when:
			PrintTools.class.newInstance()
		then:
			IllegalStateException e = thrown()
			e.message == "Utility classes should never be constructed"
	}

	def "an object should be converted to pretty json" () {
		given:
			def object = ["key":"value"]
		when:
			def result = PrintTools.prettyPrint(object, objectMapper)
		then:
			result.contains(""""key" : "value""")
	}

	def "a set of objects should be converted to pretty json" () {
		given:
			def object1 = ["captain":"america"]
			def object2 = ["iron":"man"]
			def object3 = ["black":"widow"]
			def objects = [object1, object2, object3] as Set
		when:
			def result = PrintTools.prettyPrint(objects, objectMapper)
		then:
			result.contains("""captain" : "america""")
			result.contains("""iron" : "man""")
			result.contains("""black" : "widow""")
	}

	def "json processing exceptions should be thrown as IllegalStateExceptions" () {
		given:
			ObjectMapper brokenMappper = Mock(ObjectMapper)
			def object = ["key":"value"]
		when:
			PrintTools.prettyPrint(object, brokenMappper)
		then:
			1 * brokenMappper.writerWithDefaultPrettyPrinter() >> {throw new JsonGenerationException("Shits broken yo!")}
			thrown(IllegalStateException)
	}


}
