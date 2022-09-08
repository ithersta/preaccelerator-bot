package ru.spbstu.preaccelerator.telegram.entities.state

import kotlinx.serialization.Serializable
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.telegram.annotations.OffsetDateTimeSerializer
import java.time.OffsetDateTime

object NewMeetingState {
    @Serializable
    object WaitingForTeam : DialogState

    @Serializable
    class WaitingForUrl(
        val teamId: Team.Id
    ): DialogState

    @Serializable
    class WaitingForTime(
        val teamId: Team.Id,
        val url: String
    ): DialogState


    @Serializable
    class CheckCorrect(
        val teamId: Team.Id,
        val url: String,
        @Serializable (with= OffsetDateTimeSerializer::class)
        val time: OffsetDateTime
    ): DialogState
}
