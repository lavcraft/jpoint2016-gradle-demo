import org.gradle.api.Plugin
import org.gradle.api.Project
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

  }
}
