package net.twisterrob.gradle.common

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware

open class BaseQualityPlugin(
		private val taskCreatorType: Class<out VariantTaskCreator<*>>,
		private val extensionName: String,
		private val extensionType: Class<out BaseQualityExtension<*>>
) : BaseExposedPlugin() {

	override fun apply(target: Project) {
		super.apply(target)

		val quality = project.extensions.findByName("quality") as ExtensionAware?
				?: project.extensions.create("quality", FakeQualityExtension::class.java) as ExtensionAware
		quality.extensions.create(extensionName, extensionType, project)

		// level of indirection with base is to prevent loading classes in project not having Android
		project.plugins.withId("com.android.base") {
			val taskCreator = taskCreatorType.newInstance(project)
			AndroidVariantApplier(project).apply(Action(taskCreator::applyTo))
		}
	}
}

private inline fun <T, reified P1> Class<T>.newInstance(p1: P1?): T =
		this.getDeclaredConstructor(P1::class.java).newInstance(p1)

private open class FakeQualityExtension
