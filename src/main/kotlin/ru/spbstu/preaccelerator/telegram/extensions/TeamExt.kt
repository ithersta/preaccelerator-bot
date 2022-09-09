package ru.spbstu.preaccelerator.telegram.extensions

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Task
import ru.spbstu.preaccelerator.domain.usecases.actions.TeamActions
import java.time.OffsetDateTime

object TeamExt : KoinComponent {
    private val actions: TeamActions by inject()
    val Team.protocol get() = with(actions) { protocol }
    val Team.availableModules get() = with(actions) { availableModules }

    fun Team.addHomework(taskNumber: Task.Number, url: String) =
        with(actions) { addHomework(taskNumber, url, OffsetDateTime.now()) }

    fun Team.getHomework(taskNumber: Task.Number) =
        with(actions) { getHomework(taskNumber) }
}
