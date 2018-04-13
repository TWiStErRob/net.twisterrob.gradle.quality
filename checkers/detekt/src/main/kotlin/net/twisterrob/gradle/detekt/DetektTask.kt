package net.twisterrob.gradle.detekt

import groovy.lang.Closure
import groovy.lang.DelegatesTo
import io.gitlab.arturbosch.detekt.DetektCheckTask
import net.twisterrob.gradle.common.ALL_VARIANTS_NAME
import net.twisterrob.gradle.common.TargetChecker
import org.gradle.api.Action
import org.gradle.api.Incubating
import org.gradle.api.internal.ClosureBackedAction
import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.reporting.Reporting
import org.gradle.api.tasks.Input
import javax.inject.Inject

open class DetektTask : DetektCheckTask(), TargetChecker, Reporting<DetektReports> {

	@Input
	override var checkTargetName: String = ALL_VARIANTS_NAME

	private val reports: DetektReports

	init {
		group = JavaBasePlugin.VERIFICATION_GROUP
		reports = this.getObjectFactory().newInstance(DetektReportsImpl::class.java, this)
//		classpath = project.files()
//		isShowViolations = false
	}

	override fun getReports() = reports

	override fun reports(
			@DelegatesTo(value = DetektReports::class, strategy = Closure.DELEGATE_FIRST) closure: Closure<*>
	): DetektReports {
		return reports(ClosureBackedAction<DetektReports>(closure))
	}

	override fun reports(configureAction: Action<in DetektReports>): DetektReports {
		configureAction.execute(reports)
		return reports
	}

	@Incubating
	@Inject
	fun getObjectFactory(): ObjectFactory {
		throw UnsupportedOperationException()
	}
}
