package ru.spbstu.preaccelerator.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.data.AppDatabase
import ru.spbstu.preaccelerator.domain.repository.SeasonStartRepository
import java.time.OffsetDateTime

@Single
class SeasonStartRepositoryImpl(
    private val appDatabase: AppDatabase
) : SeasonStartRepository {
    override fun get(): OffsetDateTime? {
        return appDatabase.seasonStartQueries.get().executeAsOneOrNull()
    }

    override fun getAsFlow(): Flow<OffsetDateTime?> {
        return appDatabase.seasonStartQueries.get().asFlow().mapToOneOrNull()
    }

    override fun set(start: OffsetDateTime) {
        appDatabase.seasonStartQueries.set(start)
    }
}
