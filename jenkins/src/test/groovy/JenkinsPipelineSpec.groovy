import javaposse.jobdsl.dsl.DslScriptLoader
import javaposse.jobdsl.dsl.GeneratedItems
import javaposse.jobdsl.dsl.JobManagement
import javaposse.jobdsl.dsl.MemoryJobManagement
import spock.lang.Shared
import spock.lang.Specification

class JenkinsPipelineSpec extends Specification {

    @Shared File configFile
    @Shared JobManagement jobManagement
    @Shared GeneratedItems items

    def 'pipeline should be processed without error' () {
        when:
            configFile = new File("src/main/groovy/JenkinsPipeline.groovy")
            jobManagement = new MemoryJobManagement()
            jobManagement.availableFiles.put(
                    'jenkins/src/main/resources/scripts/execute_release.sh',
                    'src/main/resources/scripts/execute_release.sh'
            )
            jobManagement.availableFiles.put(
                'jenkins/src/main/resources/scripts/documentation_update.sh',
                'src/main/resources/scripts/documentation_update.sh'
            )

            items = DslScriptLoader.runDslEngine(configFile.text, jobManagement)
        then:
            noExceptionThrown()
    }

}
