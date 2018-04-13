package net.twisterrob.gradle.detekt

import org.gradle.api.Task
import org.gradle.api.reporting.ReportContainer
import org.gradle.api.reporting.SingleFileReport
import org.gradle.api.reporting.internal.TaskGeneratedSingleFileReport
import org.gradle.api.reporting.internal.TaskReportContainer
import org.gradle.api.tasks.Internal
import javax.inject.Inject

interface DetektReports : ReportContainer<SingleFileReport> {

	@get:Internal
	val html: SingleFileReport

	@get:Internal
	val xml: SingleFileReport
}

internal class DetektReportsImpl @Inject constructor(
		task: Task
) : TaskReportContainer<SingleFileReport>(SingleFileReport::class.java, task), DetektReports {

	init {
		add(TaskGeneratedSingleFileReport::class.java, "html", task)
		add(TaskGeneratedSingleFileReport::class.java, "xml", task)
	}

	override val html: SingleFileReport get() = getByName("html")
	override val xml: SingleFileReport get() = getByName("xml")
}
