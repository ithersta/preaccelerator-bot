package ru.spbstu.preaccelerator.telegram.extensions

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.user.Member
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.domain.usecases.actions.TrackerActions

object TrackerExt : KoinComponent {
    private val actions: TrackerActions by inject()

    val Tracker.teams get() = with(actions) { teams }
    val Tracker.userId get() = with(actions) { userId }
}
