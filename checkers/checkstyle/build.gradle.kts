plugins {
	`groovy`
	`java-gradle-plugin`
}

apply {
	plugin("kotlin")
}

base.archivesBaseName = "twister-quality-checkstyle"

val VERSION_ANDROID_PLUGIN by project
val VERSION_JUNIT by project
val VERSION_JETBRAINS_ANNOTATIONS by project

dependencies {
	implementation(project(":common"))

	compileOnly("com.android.tools.build:gradle:${VERSION_ANDROID_PLUGIN}")

	testImplementation(project(":test"))
	testImplementation("junit:junit:${VERSION_JUNIT}")
	testImplementation("org.jetbrains:annotations:${VERSION_JETBRAINS_ANNOTATIONS}")
}

pullTestResourcesFrom(":test")