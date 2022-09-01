package ru.spbstu.preaccelerator.telegram.entities.state

import kotlinx.serialization.Contextual
import ru.spbstu.preaccelerator.domain.entities.module.Module
import kotlinx.serialization.Serializable

@Serializable
class ModuleState(
    @Contextual
    val moduleNumber: Module.Number,
    val partIndex: Int
) : DialogState

@Serializable
object StartModule : DialogState