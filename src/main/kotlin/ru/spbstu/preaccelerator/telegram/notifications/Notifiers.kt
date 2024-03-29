package ru.spbstu.preaccelerator.telegram.notifications

import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import org.koin.core.annotation.Single

@Single
class Notifiers(
    private val moduleDeadlineNotifier: ModuleDeadlineNotifier,
    private val protocolDeadlineNotifier: ProtocolDeadlineNotifier,
    private val meetingNotifier: MeetingNotifier
) {
    fun BehaviourContext.start() {
        with(moduleDeadlineNotifier) { setupScheduler() }
        with(protocolDeadlineNotifier) { setupScheduler() }
        with(meetingNotifier) { setupScheduler() }
    }
}
