import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.tasks.bundling.Zip
import org.gradle.model.ModelMap
import org.gradle.model.Mutate
import org.gradle.model.RuleSource
/**
 * Created by aleksandr on 16.04.16.
 */
class DocumentationRules extends RuleSource {
    @Mutate void setup(Project project) {
        documentation.name = 'mydoc'
    }

    @Mutate void createTasks(ModelMap<Task> tasks, Documentation documentation, ExtensionContainer extensionContainer) {
        tasks.create("distributionZipNew", Zip) {
            group documentation.name
            description 'bla bla bla'
        }
    }
}
