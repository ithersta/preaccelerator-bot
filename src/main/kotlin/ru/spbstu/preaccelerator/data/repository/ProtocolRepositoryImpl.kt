package ru.spbstu.preaccelerator.data.repository

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.data.AppDatabase
import ru.spbstu.preaccelerator.domain.entities.Protocol
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.repository.ProtocolRepository

@Single
class ProtocolRepositoryImpl(private val appDatabase: AppDatabase) : ProtocolRepository {
    override fun get(teamId: Team.Id): Protocol? {
        return appDatabase.protocolQueries.get(teamId).executeAsOneOrNull()?.toDomainModel()
    }

    override fun add(teamId: Team.Id, url: String) {
        appDatabase.protocolQueries.add(teamId, url)
    }

    private fun ru.spbstu.preaccelerator.data.Protocol.toDomainModel() = Protocol(
        teamId = teamId,
        url = url
    )

}
