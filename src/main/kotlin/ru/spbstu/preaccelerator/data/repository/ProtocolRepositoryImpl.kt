package ru.spbstu.preaccelerator.data.repository

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.data.AppDatabase
import ru.spbstu.preaccelerator.domain.entities.Meeting
import ru.spbstu.preaccelerator.domain.entities.Protocol
import ru.spbstu.preaccelerator.domain.repository.ProtocolRepository

@Single
class ProtocolRepositoryImpl(
    private val appDatabase: AppDatabase
) : ProtocolRepository {
    override fun get(meetingId: Meeting.Id): Protocol {
        return appDatabase.protocolQueries.get(meetingId).executeAsOne().toDomainModel()
    }

    override fun add(meetingId: Meeting.Id, url: String) {
        appDatabase.protocolQueries.add(meetingId, url)
    }

    private fun ru.spbstu.preaccelerator.data.Protocol.toDomainModel() = Protocol(
        meetingId = meetingId, url = url
    )
}

