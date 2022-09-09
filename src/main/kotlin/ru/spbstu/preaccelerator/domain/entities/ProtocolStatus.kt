package ru.spbstu.preaccelerator.domain.entities

import ru.spbstu.preaccelerator.domain.entities.module.Module

class ProtocolStatus(
    val teamId: Team.Id,
    val moduleNumber: Module.Number,
    val value: Value = Value.Unsent,
    val comment: String? = null
) {
    enum class Value {
        Unsent, Sent, Accepted, Declined
    }
}

fun ProtocolStatus.isFinished() = value in listOf(ProtocolStatus.Value.Sent, ProtocolStatus.Value.Accepted)
