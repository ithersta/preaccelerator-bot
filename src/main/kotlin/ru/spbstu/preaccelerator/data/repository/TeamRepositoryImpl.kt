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
    override fun getAll(): List<Team> {
        return appDatabase.teamQueries.getAll().executeAsList().map { it.toDomainModel() }
    }

    override fun get(id: Team.Id): Team {
        return appDatabase.teamQueries.get(id).executeAsOne().toDomainModel()
    }

    override fun get(trackerId: Tracker.Id): List<Team> {
        return appDatabase.teamQueries.getByTrackerId(trackerId).executeAsList().map { it.toDomainModel() }
    }

    override fun get(name: String): Team? {
        return appDatabase.teamQueries.getByName(name).executeAsOneOrNull()?.toDomainModel()
    }

    override fun add(name: String, trackerId: Tracker.Id): Team.Id {
        return appDatabase.teamQueries.add(name, trackerId).executeAsOne()
    }

    override fun getPaginatedWithSentProtocols(offset: Int, limit: Int): List<Team> {
        return appDatabase.teamQueries.getPaginatedWithSentProtocols(limit.toLong(), offset.toLong()).executeAsList()
            .map { it.toDomainModel() }
    }

    override fun countWithSentProtocols(): Int {
        return appDatabase.teamQueries.countWithSentProtocols().executeAsOne().toInt()
    }

    override fun getByTrackerIdPaginated(trackerId: Tracker.Id, offset: Int, limit: Int): List<Team> {
        return appDatabase.teamQueries
            .getByTrackerIdPaginated(trackerId, offset = offset.toLong(), limit = limit.toLong())
            .executeAsList().map { it.toDomainModel() }
    }

    override fun countByTrackerId(trackerId: Tracker.Id): Int {
        return appDatabase.teamQueries.countByTrackerId(trackerId).executeAsOne().toInt()
    }

    private fun ru.spbstu.preaccelerator.data.Team.toDomainModel() = Team(
        id = id,
        name = name,
        trackerId = trackerId
    )
}
