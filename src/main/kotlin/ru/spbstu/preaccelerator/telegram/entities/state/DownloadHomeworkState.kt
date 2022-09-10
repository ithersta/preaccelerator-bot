package ru.spbstu.preaccelerator.telegram.entities.state

import kotlinx.serialization.Serializable
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module

@Serializable
object DownloadHomeworkState:DialogState

@Serializable
class WaitingForModuleSpecification(
    val team: Team.Id
):DialogState

@Serializable
class ModuleHomework(
    val team: Team.Id,
    val moduleNumber: Module.Number
):DialogState


@Serializable
class AllModules(
    val team: Team.Id
):DialogState


