package ru.spbstu.preaccelerator.telegram.entities.state

import kotlinx.serialization.Serializable
import ru.spbstu.preaccelerator.domain.entities.Protocol
import ru.spbstu.preaccelerator.domain.entities.ProtocolStatus
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module.Number

class ProtocolState {
    @Serializable
    object ChooseTeam : DialogState

    @Serializable
    class ChooseModule(val teamId: Team.Id) : DialogState

    @Serializable
    class SendDiskUrl(val teamId: Team.Id, val moduleNumber: Number) : DialogState
    @Serializable
    class FixWrongProtocol(val teamId: Team.Id, val moduleNumber: Number) : DialogState

    @Serializable
    class ChooseProtocol(val teamId: Team.Id, val moduleNumber: Number, val protocol: Protocol?) : DialogState

    @Serializable
    class NotificationButton(val teamId: Team.Id, val moduleNumber: Number, val urlOrProtocol: String) : DialogState
}