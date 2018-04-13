package net.twisterrob.gradle.common

import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.internal.file.pattern.PatternMatcherFactory
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.util.PatternSet

open class BaseQualityExtension<T>(
		private val project: Project,
		internal var taskConfigurator: Action<TaskConfigurator<*>> = Action {}
) where T : DefaultTask {

	fun taskConfigurator(closure: Action<TaskConfigurator<*>>) {
		taskConfigurator = closure
	}
}

class TaskConfigurator<out T>(val task: T) where T : SourceTask {
	/**
	 * @param basePrefix Directory pattern to exclude, no trailing slash
	 * @param relativeExceptions relative to `basePrefix`, no trailing slash
	 */
	fun excludeExcept(basePrefix: String, vararg relativeExceptions: String) {
		val exactException = PatternMatcherFactory
				.getPatternMatcher(false, true, "${basePrefix}/*")
		val exceptions = PatternSet()
				.include("${basePrefix}/*")
				.exclude(relativeExceptions.map { "${basePrefix}/${it}" })
		task.source.matching(exceptions).visit {
			if (it.isDirectory && exactException.isSatisfiedBy(it.relativePath)) {
				task.exclude(it.relativePath.pathString + "/**")
			}
		}
	}
}
