package org.asciidoctor.extension

import org.asciidoctor.ast.DocumentRuby
import org.gradle.api.Project

import java.time.ZonedDateTime

/**
 * @author tolkv
 * @since 12/04/16
 */
class DependencyIncludeProcessor extends IncludeProcessor {
  //TODO WTF .. we need to avoid static project set... However, maybe it is right way :)
  @Delegate
  public static Project project
  public static boolean debug

  DependencyIncludeProcessor() {
    super([:])
  }

  public DependencyIncludeProcessor(Map<String, Object> config) {
    super(config)
  }

  @Override
  boolean handles(String target) {
    return target.startsWith('gradle://')
  }

  @Override
  void process(DocumentRuby document, PreprocessorReader reader, String target, Map<String, Object> attributes) {
    def dependency = target.substring(target.indexOf(':') + 3)
    def split = dependency.split('/')

    def dependencyName = split[0]
    def dependencyInsideFileName = split[1]

    String content

    def artifact

    artifact = configurations.docs.resolvedConfiguration.resolvedArtifacts.find {
      def version = it.moduleVersion.id

      if (dependencyName.startsWith(':'))
        return version.name == dependencyName.replace(':', '')
      else {
        return dependencyName == version.toString()
      }
    }

    if (!artifact) {
      logger.warn 'Dependency {} not found', dependencyName
      return
    }

    def file = artifact.file

    if (!file.exists()) {
      logger.warn 'Dependency represented in file {} not found', file?.absolutePath
      return
    }

    File fileWithTargetContent = zipTree(file).find {
      it.name == dependencyInsideFileName
    } as File

    if (!fileWithTargetContent) {
      logger.warn 'File {} has not found in dependency {}', dependencyInsideFileName, dependencyName
      return
    } else {
      content = fileWithTargetContent.text
    }

    String metadataWithContent
    if (debug) {
      metadataWithContent = """
    time:                     ${ZonedDateTime.now()}
    project:                  ${name}
    document:                 $document
    dependencyName:           $dependencyName
    dependencyInsideFileName: $dependencyInsideFileName
    attributes:               $attributes
    dependencies:

$content
    """
    } else {
      metadataWithContent = content
    }

    reader.push_include(metadataWithContent, target, target, 1, attributes)
  }
}