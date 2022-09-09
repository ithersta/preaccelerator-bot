package ru.spbstu.preaccelerator.telegram.entities.state

import kotlinx.serialization.Serializable
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module.Number

@Serializable
object ChooseTeam : DialogState

@Serializable
class ChooseModule(val teamId: Team.Id) : DialogState


@Serializable
class SendDiskUrl(val teamId: Team.Id, val moduleNumber: Number) : DialogState

@Serializable
class fillInTheProtocolWithData(val teamId: Team.Id, val moduleNumber: Number, val url: String) : DialogState

@Serializable
object CheckCorrectInput : DialogState

@Serializable
object FinalSendProtocolState : DialogState