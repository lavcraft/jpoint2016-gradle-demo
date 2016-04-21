import org.gradle.api.Task
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.tasks.bundling.Zip
import org.gradle.model.ModelMap
import org.gradle.model.Mutate
import org.gradle.model.RuleSource

/**
 * @author aleksandr
 * @since 16.04.16
 */
class DocumentationRules extends RuleSource {
  @Mutate
  void setup(Documentation documentation) {
    documentation.name = 'mydoc'
  }

  @Mutate
  void createTasks(ModelMap<Task> tasks, Documentation documentation, ExtensionContainer extensionContainer) {
    tasks.create("distributionZipNew", Zip) {
      group documentation.name
      description 'About distributionZipNew'
    }
  }
}
