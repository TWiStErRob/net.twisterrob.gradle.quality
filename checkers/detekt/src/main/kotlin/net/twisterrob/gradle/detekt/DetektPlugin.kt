package net.twisterrob.gradle.detekt

import net.twisterrob.gradle.common.BaseQualityPlugin

class DetektPlugin : BaseQualityPlugin(
		DetektTaskCreator::class.java,
		"detekt",
		DetektExtension::class.java
)
