package ru.spbstu.preaccelerator.domain.entities

import ru.spbstu.preaccelerator.domain.entities.module.Module

class ProtocolStatus(
    val teamId: Team.Id,
    val moduleNumber: Module.Number,
    val value: Value,
    val comment: String?
) {
    enum class Value {
        Unsent, Sent, Accepted, Declined
    }
}
