plugins {
//	kotlin("jvm")
	`java-gradle-plugin`
	id("com.gradle.plugin-publish") version "0.9.10"
}

base.archivesBaseName = "twister-quality"

val VERSION_ANDROID_PLUGIN: String by project
val VERSION_VIOLATIONS: String by project
val VERSION_JUNIT: String by project
val VERSION_HAMCREST: String by project
val VERSION_MOCKITO: String by project
val VERSION_JETBRAINS_ANNOTATIONS: String by project
val VERSION_XML_BUILDER: String by project

dependencies {
	implementation(project(":common"))
	implementation(project(":checkstyle"))
	implementation(project(":pmd"))

	compileOnly("com.android.tools.build:gradle:${VERSION_ANDROID_PLUGIN}")
//	compileOnly ("de.aaschmid:gradle-cpd-plugin:1.0")
	implementation("se.bjurr.violations:violations-lib:${VERSION_VIOLATIONS}")
	implementation("org.redundent:kotlin-xml-builder:${VERSION_XML_BUILDER}")

	testImplementation(gradleTestKit())
	testImplementation(project(":test"))

	testImplementation("junit:junit:${VERSION_JUNIT}")
	testImplementation("org.hamcrest:hamcrest-all:${VERSION_HAMCREST}")
	testImplementation("org.mockito:mockito-core:${VERSION_MOCKITO}")
	testImplementation("org.jetbrains:annotations:${VERSION_JETBRAINS_ANNOTATIONS}")
}

listOf( ":test", ":checkstyle", ":pmd" ).forEach(project::pullTestResourcesFrom)

pluginBundle {
	website = "https://github.com/TWiStErRob/net.twisterrob.gradle"
	vcsUrl = "https://github.com/TWiStErRob/net.twisterrob.gradle"

	description = "Quality plugin for Gradle that supports Android flavors."
	tags = listOf("quality", "android", "multiproject", "android-flavors", "java", "checkstyle", "pmd")

	(plugins) {
		"quality" {
			id = "net.twisterrob.quality"
			displayName = "Quality tasks generator for Android Gradle projects"
		}
	}

	mavenCoordinates {
		groupId = rootProject.group as String
		artifactId = base.archivesBaseName
	}
}
