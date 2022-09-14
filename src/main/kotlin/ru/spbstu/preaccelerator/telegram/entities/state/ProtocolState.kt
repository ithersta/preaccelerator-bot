package ru.spbstu.preaccelerator.telegram.entities.state

import kotlinx.serialization.Serializable
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module.Number

class ProtocolState {
    @Serializable
    object ChooseTeam : DialogState

    @Serializable
    class ChooseModule(val teamId: Team.Id) : DialogState

    @Serializable
    class SendUrl(val teamId: Team.Id, val moduleNumber: Number) : DialogState

    @Serializable
    class FixWrong(val teamId: Team.Id, val moduleNumber: Number) : DialogState

    @Serializable
    class CheckStatus(val teamId: Team.Id, val moduleNumber: Number) : DialogState

    @Serializable
    class NotificationButton(val teamId: Team.Id, val moduleNumber: Number, val url: String) : DialogState
}
