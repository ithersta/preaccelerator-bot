package ru.spbstu.preaccelerator.telegram.extensions

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.usecases.actions.TeamActions

object TeamExt : KoinComponent {
    private val actions: TeamActions by inject()

    val Team.availableModules get() = with(actions) { availableModules }
}
