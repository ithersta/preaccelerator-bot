package ru.spbstu.preaccelerator.telegram.entities.state

import kotlinx.serialization.Serializable
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module
import ru.spbstu.preaccelerator.telegram.annotations.OffsetDateTimeSerializer
import java.time.OffsetDateTime

object NewMeetingState {

    @Serializable
    object WaitingForModuleNumber : DialogState

    @Serializable
    class WaitingForTeam(
        val moduleNumber: Module.Number
    ): DialogState

    @Serializable
    class WaitingForUrl(
        val moduleNumber: Module.Number,
        val teamId: Team.Id
    ): DialogState

    @Serializable
    class WaitingForTime(
        val moduleNumber: Module.Number,
        val teamId: Team.Id,
        val url: String
    ): DialogState


    @Serializable
    class CheckCorrect(
        val moduleNumber: Module.Number,
        val teamId: Team.Id,
        val url: String,
        @Serializable (with= OffsetDateTimeSerializer::class)
        val time: OffsetDateTime
    ): DialogState

    @Serializable
    class WaitingForApproval(
        val moduleNumber: Module.Number,
        val teamId: Team.Id,
        val url: String,
        @Serializable (with= OffsetDateTimeSerializer::class)
        val time: OffsetDateTime
    ): DialogState

}
