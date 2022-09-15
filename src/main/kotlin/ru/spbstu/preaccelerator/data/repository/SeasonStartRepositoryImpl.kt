package ru.spbstu.preaccelerator.data.repository

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.data.AppDatabase
import ru.spbstu.preaccelerator.domain.repository.SeasonStartRepository
import java.time.OffsetDateTime

@Single
class SeasonStartRepositoryImpl(
    private val appDatabase: AppDatabase
) : SeasonStartRepository {
    private val updateChannel = Channel<Unit>(CONFLATED)

    override fun get(): OffsetDateTime? {
        return appDatabase.seasonStartQueries.get().executeAsOneOrNull()
    }

    override fun getAsFlow(): Flow<OffsetDateTime?> {
        return flow {
            updateChannel.trySend(Unit)
            for (item in updateChannel) {
                emit(appDatabase.seasonStartQueries.get().executeAsOneOrNull())
            }
        }
    }

    override fun set(start: OffsetDateTime) {
        appDatabase.seasonStartQueries.set(start).also {
            updateChannel.trySend(Unit)
        }
    }
}
