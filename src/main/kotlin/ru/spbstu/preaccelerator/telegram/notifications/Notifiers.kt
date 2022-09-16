package ru.spbstu.preaccelerator.telegram.notifications

import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import org.koin.core.annotation.Single
import org.quartz.impl.StdSchedulerFactory

@Single
class Notifiers(
    private val moduleDeadlineNotifier: ModuleDeadlineNotifier,
    private val protocolDeadlineNotifier: ProtocolDeadlineNotifier,
    private val meetingNotifier: MeetingNotifier
) {
    fun BehaviourContext.start() {
        val scheduler = StdSchedulerFactory.getDefaultScheduler()
        with(moduleDeadlineNotifier) { setupJobs(scheduler) }
        with(protocolDeadlineNotifier) { setupJobs(scheduler) }
        with(meetingNotifier) { setupJobs(scheduler) }
        scheduler.start()
    }
}
