package ru.spbstu.preaccelerator.data.repository

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.data.AppDatabase
import ru.spbstu.preaccelerator.domain.entities.Meeting
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.user.Member
import ru.spbstu.preaccelerator.domain.repository.MeetingRepository
import java.time.OffsetDateTime

@Single
class MeetingRepositoryImpl(private val appDatabase: AppDatabase) : MeetingRepository {
    override fun get(id: Meeting.Id): Meeting {
        return appDatabase.meetingQueries.get(id).executeAsOne().toDomainModel()
    }

    override fun get(teamId: Team.Id): List<Meeting> {
        return appDatabase.meetingQueries.getByTeamId(teamId).executeAsList().map { it.toDomainModel() }
    }

    override fun add(teamId: Team.Id, timestamp: OffsetDateTime, url: String): Meeting.Id {
        return appDatabase.meetingQueries.add(teamId, timestamp, url).executeAsOne()
    }

    private fun ru.spbstu.preaccelerator.data.Meeting.toDomainModel() = Meeting(
        id = id,
        teamId = teamId,
        timestamp = timestamp,
        url = url
    )
}