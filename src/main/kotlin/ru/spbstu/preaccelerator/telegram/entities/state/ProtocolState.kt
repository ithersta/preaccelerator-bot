package ru.spbstu.preaccelerator.telegram.entities.state

import dev.inmo.tgbotapi.types.MessageIdentifier
import kotlinx.serialization.Serializable
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module.Number

class ProtocolState {
    @Serializable
    class ChooseTeam(
        val messageId: MessageIdentifier? = null
    ) : DialogState

    @Serializable
    class ChooseModule(
        val teamId: Team.Id,
        val messageId: MessageIdentifier?
    ) : DialogState

    @Serializable
    class WaitingForUrl(
        val teamId: Team.Id,
        val moduleNumber: Number,
        val returnTo: DialogState
    ) : DialogState

    @Serializable
    class FixWrong(val teamId: Team.Id, val moduleNumber: Number) : DialogState

    @Serializable
    class NotificationButton(val teamId: Team.Id, val moduleNumber: Number, val url: String) : DialogState
}
