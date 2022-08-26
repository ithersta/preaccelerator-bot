package ru.spbstu.preaccelerator.domain.entities

import java.time.Instant

class Meeting(
    val id: Id,
    val teamId: Team.Id,
    val timestamp: Instant,
    val url: String
) {
    @JvmInline
    value class Id(val value: Long)
}
