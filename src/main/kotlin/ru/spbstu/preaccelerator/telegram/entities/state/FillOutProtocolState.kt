package ru.spbstu.preaccelerator.telegram.entities.state

import dev.inmo.tgbotapi.types.MessageIdentifier
import kotlinx.serialization.Serializable
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module.Number

class FillOutProtocolState {
    @Serializable
    object ChooseTeam : DialogState

    @Serializable
    class WaitingForUrl(
        val teamId: Team.Id,
        val moduleNumber: Number,
        val messageId: MessageIdentifier,
        val page: Int,
        val returnTo: DialogState
    ) : DialogState

    @Serializable
    class FixWrong(
        val teamId: Team.Id,
        val moduleNumber: Number,
        val messageId: MessageIdentifier,
        val page: Int,
        val returnTo: DialogState
    ) : DialogState

    @Serializable
    class NotificationButton(
        val teamId: Team.Id,
        val moduleNumber: Number,
        val messageId: MessageIdentifier,
        val page: Int,
        val returnTo: DialogState
    ) : DialogState
}
