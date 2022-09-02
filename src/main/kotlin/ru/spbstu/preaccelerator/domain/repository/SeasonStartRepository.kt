package ru.spbstu.preaccelerator.domain.repository

import java.time.OffsetDateTime

interface SeasonStartRepository {
    fun get(): OffsetDateTime?
    fun set(start: OffsetDateTime)
}
