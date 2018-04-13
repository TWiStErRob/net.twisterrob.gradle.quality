package net.twisterrob.gradle.detekt

import net.twisterrob.gradle.common.VariantTaskCreator
import org.gradle.api.Project

class DetektTaskCreator(project: Project) : VariantTaskCreator<DetektTask>(
		project,
		"detekt",
		"detekt",
		DetektTask::class.java,
		DetektExtension::class.java
)
