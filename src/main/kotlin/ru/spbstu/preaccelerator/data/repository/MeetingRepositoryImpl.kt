package ru.spbstu.preaccelerator.data.repository

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.data.AppDatabase
import ru.spbstu.preaccelerator.domain.entities.Meeting
import ru.spbstu.preaccelerator.domain.entities.module.Module
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.domain.repository.MeetingRepository

@Single
class MeetingRepositoryImpl(
    private val appDatabase: AppDatabase
) : MeetingRepository {
    override fun getFirst(trackerId: Tracker.Id, moduleNumber: Module.Number): Meeting? {
        return appDatabase.meetingQueries.getFirst(trackerId, moduleNumber).executeAsOneOrNull()?.toDomainModel()
    }

    private fun ru.spbstu.preaccelerator.data.Meeting.toDomainModel() = Meeting(
        id = id,
        teamId = teamId,
        moduleNumber = moduleNumber,
        timestamp = timestamp,
        url = url
    )
}
