package ru.spbstu.preaccelerator.domain.repository

import kotlinx.coroutines.flow.Flow
import java.time.OffsetDateTime

interface SeasonStartRepository {
    fun get(): OffsetDateTime?
    fun getAsFlow(): Flow<OffsetDateTime?>
    fun set(start: OffsetDateTime)
}
