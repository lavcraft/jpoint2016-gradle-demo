import nebula.test.IntegrationSpec
import nebula.test.functional.ExecutionResult

/**
 * Created by aleksandr on 18.04.16.
 */
class NebulaTestSpecification extends IntegrationSpec {
  def setup() {
    buildFile << """
            buildscript {
              repositories { jcenter() }
              dependencies {
                classpath 'org.asciidoctor:asciidoctor-gradle-plugin:1.5.3'
              }
            }

            apply plugin: 'org.asciidoctor.convert'
            apply plugin: DocumentationPlugin

            docs {
              debug = true
            }

            dependencies {
              asciidoctor 'org.asciidoctor:asciidoctorj:1.5.4'
              docs 'org.slf4j:slf4j-api:1.7.2'
            }
"""
  }

  def "execution of documentation distribution task is success"() {
    when:
    createFile("/src/docs/asciidoc/documentation.adoc")
    ExecutionResult executionResult = runTasksSuccessfully('documentationDistZip')

    then:
    executionResult.wasExecuted('documentationDistZip')
  }
}
