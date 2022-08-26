package ru.spbstu.preaccelerator.data.repository

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.data.AppDatabase
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.domain.repository.TeamRepository

@Single
class TeamRepositoryImpl(
    private val appDatabase: AppDatabase
) : TeamRepository {
    override fun get(id: Team.Id): Team {
        return appDatabase.teamQueries.get(id).executeAsOne().toDomainModel()
    }

    override fun get(trackerId: Tracker.Id): List<Team> {
        return appDatabase.teamQueries.getByTrackerId(trackerId).executeAsList().map { it.toDomainModel() }
    }

    override fun add(name: String, trackerId: Tracker.Id): Team.Id {
        return appDatabase.teamQueries.add(name, trackerId).executeAsOne()
    }

    private fun ru.spbstu.preaccelerator.data.Team.toDomainModel() = Team(
        id = id,
        name = name,
        trackerId = trackerId
    )
}
