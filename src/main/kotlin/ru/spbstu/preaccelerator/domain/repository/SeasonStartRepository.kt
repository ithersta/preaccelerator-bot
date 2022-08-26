package ru.spbstu.preaccelerator.domain.repository

import java.time.Instant
import java.time.OffsetDateTime

interface SeasonStartRepository {
    fun get(): OffsetDateTime?
    fun set(start: OffsetDateTime)
}
