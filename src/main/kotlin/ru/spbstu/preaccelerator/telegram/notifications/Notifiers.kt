package ru.spbstu.preaccelerator.telegram.notifications

import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import org.koin.core.annotation.Single

@Single
class Notifiers(
    private val moduleDeadlineNotifier: ModuleDeadlineNotifier
) {
    fun BehaviourContext.start() {
        with(moduleDeadlineNotifier) { start() }
    }
}
