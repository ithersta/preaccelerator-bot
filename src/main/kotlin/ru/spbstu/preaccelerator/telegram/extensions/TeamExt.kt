package ru.spbstu.preaccelerator.telegram.extensions

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module
import ru.spbstu.preaccelerator.domain.entities.module.Task
import ru.spbstu.preaccelerator.domain.usecases.actions.TeamActions
import java.time.OffsetDateTime

object TeamExt : KoinComponent {
    private val actions: TeamActions by inject()

    val Team.members get() = with(actions) { members }
    val Team.meetings get() = with(actions) { meetings }
    val Team.availableModules get() = with(actions) { availableModules }
    val Team.protocol get() = with(actions) { protocol }

    fun Team.addHomework(taskNumber: Task.Number, url: String) =
        with(actions) { addHomework(taskNumber, url, OffsetDateTime.now()) }

    fun Team.getHomework(taskNumber: Task.Number) =
        with(actions) { getHomework(taskNumber) }

    fun Team.getProtocolStatus(moduleNumber: Module.Number) =
        with(actions) { getProtocolStatus(moduleNumber) }
}
