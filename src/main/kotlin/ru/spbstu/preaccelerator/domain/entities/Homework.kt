package ru.spbstu.preaccelerator.domain.entities

import java.time.OffsetDateTime

class Homework(
    val id: Id,
    val teamId: Team.Id,
    val url: String,
    val timestamp: OffsetDateTime
) {
    @JvmInline
    value class Id(val value: Long)
}
