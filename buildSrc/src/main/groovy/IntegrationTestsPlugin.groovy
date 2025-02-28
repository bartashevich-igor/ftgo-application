import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

class IntegrationTestsPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.apply(plugin: 'eclipse')

        project.sourceSets {
            integrationTest {
                java {
                    compileClasspath += main.output + test.output
                    runtimeClasspath += main.output + test.output
                    srcDir project.file('src/integration-test/java')
                }
                resources.srcDir project.file('src/integration-test/resources')
            }
        }

        project.configurations {
            integrationTestCompile.extendsFrom testImplementation
            integrationTestRuntime.extendsFrom testRuntime
        }

        project.eclipse.classpath.plusConfigurations << project.configurations.integrationTestCompile

        project.task("integrationTest", type: Test) {
            testClassesDirs = project.sourceSets.integrationTest.output.classesDirs
            classpath = project.sourceSets.integrationTest.runtimeClasspath
        }

        project.tasks.withType(Test) {
            reports.html.destination = project.file("${project.reporting.baseDir}/${name}")
        }
    }
}
