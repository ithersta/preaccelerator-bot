package ru.spbstu.preaccelerator.telegram.entities.state

import kotlinx.serialization.Serializable
import ru.spbstu.preaccelerator.domain.entities.module.Module

@Serializable
class ModuleState(
    val moduleNumber: Module.Number,
    val partIndex: Int
) : DialogState

@Serializable
object StartModule : DialogState
