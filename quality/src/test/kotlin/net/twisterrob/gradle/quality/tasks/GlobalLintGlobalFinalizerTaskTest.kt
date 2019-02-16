package net.twisterrob.gradle.quality.tasks

import net.twisterrob.gradle.test.GradleRunnerRule
import net.twisterrob.gradle.test.assertHasOutputLine
import net.twisterrob.gradle.test.assertNoOutputLine
import net.twisterrob.gradle.test.runBuild
import net.twisterrob.gradle.test.runFailingBuild
import org.gradle.testkit.runner.TaskOutcome
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasItems
import org.intellij.lang.annotations.Language
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class GlobalLintGlobalFinalizerTaskTest {
	@Rule @JvmField val gradle = GradleRunnerRule()

	@Test fun `passes when no failures`() {
		val modules: Array<String> = arrayOf(
			"module1",
			"module2",
			"module3"
		)
		modules.forEach { module ->
			@Language("gradle")
			val subProject = """
				apply plugin: 'com.android.library'
				android {
					lintOptions {
						check = [] // nothing
					}
				}
			""".trimIndent()

			@Language("xml")
			val manifest = """
				<manifest package="project.${module}" />
			""".trimIndent()

			gradle.file(subProject, module, "build.gradle")
			gradle.settingsFile.appendText("include ':${module}'${endl}")
			gradle.file(manifest, module, "src", "main", "AndroidManifest.xml")
		}

		@Language("gradle")
		val script = """
			subprojects {
				repositories { google() } // needed for com.android.tools.lint:lint-gradle resolution
			}
			task('lint', type: ${GlobalLintGlobalFinalizerTask::class.java.name})
		""".trimIndent()

		val result = gradle.runBuild {
			basedOn("android-multi_module")
			run(script, "lint")
		}

		val lintTasks = result.tasks.map { it.path }.filter { it.endsWith(":lint") }
		assertThat(lintTasks, hasItems(":module1:lint", ":module2:lint", ":module3:lint"))
		assertThat(lintTasks.last(), equalTo(":lint"))
		assertEquals(TaskOutcome.SUCCESS, result.task(":module1:lint")!!.outcome)
		assertEquals(TaskOutcome.SUCCESS, result.task(":module2:lint")!!.outcome)
		assertEquals(TaskOutcome.SUCCESS, result.task(":module3:lint")!!.outcome)
		assertEquals(TaskOutcome.SUCCESS, result.task(":lint")!!.outcome)
		result.assertNoOutputLine(Regex(""".*Ran lint on subprojects.*"""))
	}

	@Test fun `gathers results from submodules`() {
		@Language("java")
		val lintViolation = """
			class LintFailure {
				void f() {
					android.util.Log.e("123456789012345678901234", "");
				}
			}
		""".trimIndent()
		val modules = arrayOf(
			"module1",
			"module2",
			"module3"
		)
		modules.forEach { module ->
			@Language("gradle")
			val subProject = """
				apply plugin: 'com.android.library'
				android {
					lintOptions {
						check 'LongLogTag'
					}
				}
			""".trimIndent()

			@Language("xml")
			val manifest = """
				<manifest package="project.${module}" />
			""".trimIndent()

			gradle.file(subProject, module, "build.gradle")
			gradle.settingsFile.appendText("include ':${module}'${endl}")
			gradle.file(lintViolation, module, "src", "main", "java", "fail1.java")
			gradle.file(manifest, module, "src", "main", "AndroidManifest.xml")
		}

		@Language("gradle")
		val script = """
			subprojects {
				repositories { google() } // needed for com.android.tools.lint:lint-gradle resolution
			}
			task('lint', type: ${GlobalLintGlobalFinalizerTask::class.java.name})
		""".trimIndent()

		val result = gradle.runFailingBuild {
			basedOn("android-multi_module")
			run(script, "lint")
		}

		val lintTasks = result.tasks.map { it.path }.filter { it.endsWith(":lint") }
		assertThat(lintTasks, hasItems(":module1:lint", ":module2:lint", ":module3:lint"))
		assertThat(lintTasks.last(), equalTo(":lint"))
		assertEquals(TaskOutcome.SUCCESS, result.task(":module1:lint")!!.outcome)
		assertEquals(TaskOutcome.SUCCESS, result.task(":module2:lint")!!.outcome)
		assertEquals(TaskOutcome.SUCCESS, result.task(":module3:lint")!!.outcome)
		assertEquals(TaskOutcome.FAILED, result.task(":lint")!!.outcome)
		result.assertHasOutputLine(Regex("""> Ran lint on subprojects: ${1 + 1 + 1} issues found"""))
	}

	@Test fun `gathers results from submodules (lazy init)`() {
		@Language("java")
		val lintViolation = """
			class LintFailure {
				void f() {
					android.util.Log.e("123456789012345678901234", "");
				}
			}
		""".trimIndent()
		val modules = arrayOf(
			"module1",
			"module2",
			"module3"
		)
		modules.forEach { module ->
			@Language("gradle")
			val subProject = """
				apply plugin: 'com.android.library'
				android {
					lintOptions {
						check 'LongLogTag'
					}
				}
			""".trimIndent()

			@Language("xml")
			val manifest = """
				<manifest package="project.${module}" />
			""".trimIndent()

			gradle.file(subProject, module, "build.gradle")
			gradle.settingsFile.appendText("include ':${module}'${endl}")
			gradle.file(lintViolation, module, "src", "main", "java", "fail1.java")
			gradle.file(manifest, module, "src", "main", "AndroidManifest.xml")
		}

		@Language("gradle")
		val script = """
			subprojects {
				repositories { google() } // needed for com.android.tools.lint:lint-gradle resolution
			}
			tasks.register('lint', ${GlobalLintGlobalFinalizerTask::class.java.name})
		""".trimIndent()

		val result = gradle.runFailingBuild {
			basedOn("android-multi_module")
			run(script, "lint")
		}

		val lintTasks = result.tasks.map { it.path }.filter { it.endsWith(":lint") }
		assertThat(lintTasks, hasItems(":module1:lint", ":module2:lint", ":module3:lint"))
		assertThat(lintTasks.last(), equalTo(":lint"))
		assertEquals(TaskOutcome.SUCCESS, result.task(":module1:lint")!!.outcome)
		assertEquals(TaskOutcome.SUCCESS, result.task(":module2:lint")!!.outcome)
		assertEquals(TaskOutcome.SUCCESS, result.task(":module3:lint")!!.outcome)
		assertEquals(TaskOutcome.FAILED, result.task(":lint")!!.outcome)
		result.assertHasOutputLine(Regex("""> Ran lint on subprojects: ${1 + 1 + 1} issues found"""))
	}

	companion object {

		private val endl = System.lineSeparator()
	}
}
