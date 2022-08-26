package ru.spbstu.preaccelerator.data.repository

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

    override fun set(start: OffsetDateTime) {
        appDatabase.seasonStartQueries.set(start)
    }
}
