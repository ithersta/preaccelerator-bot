package ru.spbstu.preaccelerator.domain.entities

import kotlinx.serialization.Serializable
import ru.spbstu.preaccelerator.domain.entities.module.Module
import java.time.OffsetDateTime

class Meeting(
    val id: Id,
    val teamId: Team.Id,
    val moduleNumber: Module.Number,
    val timestamp: OffsetDateTime,
    val url: String
) {
    @Serializable
    @JvmInline
    value class Id(val value: Long)
}
