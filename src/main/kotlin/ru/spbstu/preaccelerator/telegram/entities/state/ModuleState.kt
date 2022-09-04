package ru.spbstu.preaccelerator.telegram.entities.state

import kotlinx.serialization.Serializable
import ru.spbstu.preaccelerator.domain.entities.module.Module
import ru.spbstu.preaccelerator.domain.entities.module.Task

@Serializable
class ModuleState(
    val moduleNumber: Module.Number,
    val partIndex: Int
) : DialogState

@Serializable
class ChooseModuleAction
    (
    val moduleNumber: Module.Number
) : DialogState

@Serializable
class WaitingForHomework(
    val returnTo: ModuleState,
    val taskNumber: Task.Number
) : DialogState
