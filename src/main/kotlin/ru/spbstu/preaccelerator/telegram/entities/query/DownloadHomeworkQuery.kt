package ru.spbstu.preaccelerator.telegram.entities.query

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module

object DownloadHomeworkQuery {
    @Serializable
    @SerialName("HomeworkBackToTeams")
    class BackToTeams(
        @SerialName("p") val page: Int
    ) : Query

    @Serializable
    @SerialName("HomeworkTeam")
    class OnTeamClicked(
        @SerialName("p") val page: Int,
        @SerialName("t") val teamId: Team.Id
    ) : Query

    @Serializable
    @SerialName("HomeworkModule")
    class OnModuleClicked(
        @SerialName("p") val page: Int,
        @SerialName("t") val teamId: Team.Id,
        @SerialName("m") val moduleNumber: Module.Number
    ) : Query
}
