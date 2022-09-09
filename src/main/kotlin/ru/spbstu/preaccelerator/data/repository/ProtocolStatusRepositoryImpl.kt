package ru.spbstu.preaccelerator.data.repository

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.data.AppDatabase
import ru.spbstu.preaccelerator.domain.entities.ProtocolStatus
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module
import ru.spbstu.preaccelerator.domain.repository.ProtocolStatusRepository

@Single
class ProtocolStatusRepositoryImpl(
    private val appDatabase: AppDatabase
) : ProtocolStatusRepository {
    override fun get(teamId: Team.Id, moduleNumber: Module.Number): ProtocolStatus {
        return appDatabase.protocolStatusQueries.get(teamId, moduleNumber).executeAsOneOrNull()?.toDomainModel()
            ?: ProtocolStatus(teamId, moduleNumber)
    }

    override fun set(teamId: Team.Id, moduleNumber: Module.Number, value: ProtocolStatus.Value): ProtocolStatus {
        return appDatabase.protocolStatusQueries.setValue(teamId, moduleNumber, value).executeAsOne().toDomainModel()
    }

    override fun set(teamId: Team.Id, moduleNumber: Module.Number, comment: String): ProtocolStatus {
        return appDatabase.protocolStatusQueries.setComment(comment, teamId, moduleNumber).executeAsOne()
            .toDomainModel()
    }

    private fun ru.spbstu.preaccelerator.data.ProtocolStatus.toDomainModel() = ProtocolStatus(
        teamId = teamId,
        moduleNumber = moduleNumber,
        value = value_,
        comment = comment
    )
}
