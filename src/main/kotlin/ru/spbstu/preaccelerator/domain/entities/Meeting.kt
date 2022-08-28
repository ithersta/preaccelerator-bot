package ru.spbstu.preaccelerator.domain.entities

import java.time.OffsetDateTime

class Meeting(
    val id: Id,
    val teamId: Team.Id,
    val timestamp: OffsetDateTime,
    val url: String
) {
    @JvmInline
    value class Id(val value: Long)
}
