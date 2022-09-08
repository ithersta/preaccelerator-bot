package ru.spbstu.preaccelerator.domain.entities

import ru.spbstu.preaccelerator.domain.entities.module.Module
import java.time.OffsetDateTime

class Meeting(
    val id: Id,
    val teamId: Team.Id,
    val moduleNumber: Module.Number,
    val timestamp: OffsetDateTime,
    val url: String
) {
    @JvmInline
    value class Id(val value: Long)
}
