package ru.spbstu.preaccelerator.telegram.entities.query

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module

object FillOutProtocolQuery {
    @Serializable
    @SerialName("ProtocolBackToTeams")
    class BackToTeams(val page: Int) : Query

    @Serializable
    @SerialName("ProtocolTeam")
    class OnTeamClicked(val page: Int, val teamId: Team.Id) : Query

    @Serializable
    @SerialName("ProtocolModule")
    class OnModuleClicked(val page: Int, val teamId: Team.Id, val moduleNumber: Module.Number) : Query
}
