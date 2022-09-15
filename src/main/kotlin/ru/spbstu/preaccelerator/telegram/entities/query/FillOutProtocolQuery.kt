package ru.spbstu.preaccelerator.telegram.entities.query

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module

object FillOutProtocolQuery {
    @Serializable
    @SerialName("ProtocolTeam")
    class OnTeamClicked(val teamId: Team.Id) : Query

    @Serializable
    @SerialName("ProtocolModule")
    class OnModuleClicked(val teamId: Team.Id, val moduleNumber: Module.Number) : Query
}
