package ru.spbstu.preaccelerator.telegram.entities.state

import dev.inmo.tgbotapi.types.MessageId
import kotlinx.serialization.Serializable
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module

object ReviewProtocolsState {
    @Serializable
    object ChooseTeam : DialogState

    @Serializable
    class New(
        val teamId: Team.Id? = null
    ) : DialogState

    @Serializable
    class WaitingForComment(
        val teamId: Team.Id,
        val moduleNumber: Module.Number,
        val messageId: MessageId,
        val returnTo: DialogState
    ) : DialogState
}
