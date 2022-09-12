package ru.spbstu.preaccelerator.telegram.entities.state

import kotlinx.serialization.Serializable
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module
import ru.spbstu.preaccelerator.telegram.serializers.OffsetDateTimeSerializer
import java.time.OffsetDateTime

object NewMeetingState {

    @Serializable
    object WaitingForModuleNumber : DialogState

    @Serializable
    class WaitingForTeam(
        val moduleNumber: Module.Number
    ) : DialogState

    @Serializable
    class WaitingForUrl(
        val moduleNumber: Module.Number,
        val teamId: Team.Id
    ) : DialogState

    @Serializable
    class WaitingForDateTime(
        val moduleNumber: Module.Number,
        val teamId: Team.Id,
        val url: String
    ) : DialogState

    @Serializable
    class CheckCorrect(
        val moduleNumber: Module.Number,
        val teamId: Team.Id,
        val url: String,
        @Serializable(with = OffsetDateTimeSerializer::class)
        val dateTime: OffsetDateTime
    ) : DialogState

}
