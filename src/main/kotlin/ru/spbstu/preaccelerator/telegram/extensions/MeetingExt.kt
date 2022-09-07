package ru.spbstu.preaccelerator.telegram.extensions

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.Meeting
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.domain.usecases.actions.MeetingActions
import ru.spbstu.preaccelerator.domain.usecases.actions.TrackerActions

object MeetingExt : KoinComponent {
    private val actions: MeetingActions by inject()

    val Meeting.protocol get() = with(actions) { protocol }

    fun Meeting.addProtocol(meetingId: Meeting.Id, url: String) {

    }
}
