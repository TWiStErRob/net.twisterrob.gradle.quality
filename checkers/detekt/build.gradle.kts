plugins {
//	kotlin("jvm")
	`java-gradle-plugin`
}

base.archivesBaseName = "twister-quality-detekt"

val VERSION_ANDROID_PLUGIN by project
val VERSION_DETEKT_PLUGIN by project
val VERSION_JUNIT by project
val VERSION_HAMCREST by project
val VERSION_JETBRAINS_ANNOTATIONS by project

repositories {
	// Detekt plugin
	maven { setUrl("https://plugins.gradle.org/m2/") }
}

dependencies {
	implementation(project(":common"))

	compileOnly("com.android.tools.build:gradle:${VERSION_ANDROID_PLUGIN}")
	implementation("gradle.plugin.io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${VERSION_DETEKT_PLUGIN}")

	testImplementation(project(":test"))
	testImplementation("junit:junit:${VERSION_JUNIT}")
	testImplementation("org.mockito:mockito-core:+")
	testImplementation("org.hamcrest:hamcrest-all:${VERSION_HAMCREST}")
	testImplementation("org.jetbrains:annotations:${VERSION_JETBRAINS_ANNOTATIONS}")
}

pullTestResourcesFrom(":test")
