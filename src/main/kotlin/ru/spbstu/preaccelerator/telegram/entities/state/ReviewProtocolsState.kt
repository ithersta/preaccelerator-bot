package ru.spbstu.preaccelerator.telegram.entities.state

import dev.inmo.tgbotapi.types.MessageIdentifier
import kotlinx.serialization.Serializable
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module

object ReviewProtocolsState {
    @Serializable
    class New(
        val teamId: Team.Id?
    ) : DialogState

    @Serializable
    class WaitingForComment(
        val teamId: Team.Id,
        val moduleNumber: Module.Number,
        val messageId: MessageIdentifier
    ) : DialogState
}
