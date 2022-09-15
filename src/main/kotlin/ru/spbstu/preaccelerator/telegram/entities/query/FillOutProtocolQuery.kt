package ru.spbstu.preaccelerator.telegram.entities.query

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module

object FillOutProtocolQuery {
    @Serializable
    @SerialName("ProtocolBackToTeams")
    class BackToTeams(
        @SerialName("p") val page: Int
    ) : Query

    @Serializable
    @SerialName("ProtocolTeam")
    class OnTeamClicked(
        @SerialName("p") val page: Int,
        @SerialName("t") val teamId: Team.Id
    ) : Query

    @Serializable
    @SerialName("ProtocolModule")
    class OnModuleClicked(
        @SerialName("p") val page: Int,
        @SerialName("t") val teamId: Team.Id,
        @SerialName("m") val moduleNumber: Module.Number
    ) : Query
}
