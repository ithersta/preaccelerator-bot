package ru.spbstu.preaccelerator.telegram.entities.state

import kotlinx.serialization.Serializable
import ru.spbstu.preaccelerator.domain.entities.Team

object NewMeetingState {
    @Serializable
    object WaitingForTeam : DialogState

    @Serializable
    class WaitingForUrl(
        val teamId: Team.Id
    ): DialogState

    @Serializable
    class WaitingFor
}
