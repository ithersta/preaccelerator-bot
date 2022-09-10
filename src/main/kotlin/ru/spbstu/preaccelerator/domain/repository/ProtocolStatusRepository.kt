package ru.spbstu.preaccelerator.domain.repository

import ru.spbstu.preaccelerator.domain.entities.ProtocolStatus
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module

interface ProtocolStatusRepository {
    fun getSent(): ProtocolStatus?
    fun get(teamId: Team.Id, moduleNumber: Module.Number): ProtocolStatus
    fun set(teamId: Team.Id, moduleNumber: Module.Number, value: ProtocolStatus.Value): ProtocolStatus
    fun set(teamId: Team.Id, moduleNumber: Module.Number, comment: String): ProtocolStatus
}
