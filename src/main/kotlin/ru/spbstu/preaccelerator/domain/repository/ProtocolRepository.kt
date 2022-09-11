package ru.spbstu.preaccelerator.domain.repository

import ru.spbstu.preaccelerator.domain.entities.Protocol
import ru.spbstu.preaccelerator.domain.entities.Team

interface ProtocolRepository {
    fun get(teamId: Team.Id): Protocol?
}
