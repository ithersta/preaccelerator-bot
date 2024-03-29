package ru.spbstu.preaccelerator.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.spbstu.preaccelerator.domain.entities.Meeting
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import java.time.OffsetDateTime

interface MeetingRepository {
    fun getAllAsFlow(): Flow<List<Meeting>>
    fun get(trackerId: Tracker.Id, moduleNumber: Module.Number): List<Meeting>
    fun get(id: Meeting.Id): Meeting
    fun get(teamId: Team.Id): List<Meeting>
    fun add(teamId: Team.Id, moduleNumber: Module.Number, timestamp: OffsetDateTime, url: String): Meeting.Id
}

