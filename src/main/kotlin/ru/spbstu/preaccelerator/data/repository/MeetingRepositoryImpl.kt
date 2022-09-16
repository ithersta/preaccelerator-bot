package ru.spbstu.preaccelerator.data.repository

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.data.AppDatabase
import ru.spbstu.preaccelerator.domain.entities.Meeting
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.domain.repository.MeetingRepository
import java.time.OffsetDateTime

@Single
class MeetingRepositoryImpl(
    private val appDatabase: AppDatabase
) : MeetingRepository {
    private val updateChannel = Channel<Unit>(CONFLATED)

    override fun getAllAsFlow(): Flow<List<Meeting>> {
        return flow {
            updateChannel.trySend(Unit)
            for (item in updateChannel) {
                emit(appDatabase.meetingQueries.getAll().executeAsList().map { it.toDomainModel() })
            }
        }
    }

    override fun get(trackerId: Tracker.Id, moduleNumber: Module.Number): List<Meeting> {
        return appDatabase.meetingQueries.getByTrackerIdAndModuleNumber(trackerId, moduleNumber)
            .executeAsList().map { it.toDomainModel() }
    }

    override fun get(id: Meeting.Id): Meeting {
        return appDatabase.meetingQueries.get(id).executeAsOne().toDomainModel()
    }

    override fun get(teamId: Team.Id): List<Meeting> {
        return appDatabase.meetingQueries.getByTeamId(teamId).executeAsList().map { it.toDomainModel() }
    }

    override fun add(teamId: Team.Id, moduleNumber: Module.Number, timestamp: OffsetDateTime, url: String): Meeting.Id {
        return appDatabase.meetingQueries.add(teamId, moduleNumber, timestamp, url).executeAsOne().also {
            updateChannel.trySend(Unit)
        }
    }

    private fun ru.spbstu.preaccelerator.data.Meeting.toDomainModel() = Meeting(
        id = id,
        teamId = teamId,
        moduleNumber = moduleNumber,
        timestamp = timestamp,
        url = url
    )
}
