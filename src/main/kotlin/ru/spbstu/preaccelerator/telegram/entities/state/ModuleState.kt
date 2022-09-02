package ru.spbstu.preaccelerator.telegram.entities.state

import dev.inmo.tgbotapi.types.MessageIdentifier
import kotlinx.serialization.Serializable
import ru.spbstu.preaccelerator.domain.entities.module.Module

@Serializable
class ModuleState(
    val moduleNumber: Module.Number,
    val partIndex: Int
) : DialogState

@Serializable
object StartModule : DialogState

@Serializable
class WaitingForHomework(
    val moduleNumber: Module.Number,
    val partIndex: Int,
    val messageId: MessageIdentifier
) : DialogState
