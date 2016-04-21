import org.asciidoctor.extension.DependencyIncludeProcessor
import org.asciidoctor.gradle.AsciidoctorPlugin
import org.asciidoctor.gradle.AsciidoctorTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Zip

/**
 * @author aleksandr
 * @author tolkv
 * @since 13.04.16
 */
class DocumentationPlugin implements Plugin<Project> {

  public static final String EXTENSION_NAME_DOCS = 'docs'
  public static final String DOCUMENTATION_CONFIGURATION_NAME = 'documentationConfiguration'
  public static final String DOCUMENTATION_DOCS_CONFIGURATION_NAME = 'docs'
  public static final String DOCUMENTATION_DIST_TASK_NAME = 'documentationDistZip'

  @Override
  void apply(Project project) {
    project.configure(project) {
      apply plugin: 'base'

      DocumentationPluginExtension extension = extensions.create(EXTENSION_NAME_DOCS, DocumentationPluginExtension, project)

      configurations.maybeCreate extension.documentationConfigurationName
      configurations.maybeCreate extension.documentationSourceConfigurationName

      def dDist = tasks.create(
          name: extension.documentationDistTaskName,
          type: Zip,
          group: 'Documentation',
          description: 'Create documentation sources archive')

      afterEvaluate {
        def dTask = tasks.findByName(extension.getDocumentationDistTaskName()).configure {
          from fileTree(dir: project.file('src/docs/'), include: '**/*.adoc')
        }

        artifacts {
          "$extension.documentationConfigurationName" dTask
        }

        plugins.withType(AsciidoctorPlugin) {
          asciidoctorj {
            version = '1.5.4'
            groovyDslVersion = '1.0.0.Alpha1'
          }

          dependencies {
            (rootProject.subprojects - project).each {
              "${extension.documentationSourceConfigurationName}" dependencies.project(path: ':' + it.name, configuration: extension.documentationConfigurationName)
            }
          }
        }

        tasks.withType(AsciidoctorTask) {
          sourceDir project.projectDir
          mustRunAfter extension.documentationDistTaskName
          sources {
            setIncludes fileTree(dir: projectDir, include: '**/*.adoc').collect { relativePath(it) }
          }

          outputDir = new File("$buildDir/docs")
          attributes(['source-highlighter': 'coderay',
                      doctype             : 'book',
                      toc                 : 'left',
                      idprefix            : '',
                      idseparator         : '-'])

          inputs.file project.configurations.docs.incoming.collect { it.files }

          doFirst {
            DependencyIncludeProcessor.project = project
          }
        }
      }
    }
  }
}
