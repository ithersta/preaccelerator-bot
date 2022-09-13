package ru.spbstu.preaccelerator.data.repository

import dev.inmo.tgbotapi.types.UserId
import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.data.AppDatabase
import ru.spbstu.preaccelerator.domain.entities.user.Curator
import ru.spbstu.preaccelerator.domain.repository.CuratorRepository

@Single
class CuratorRepositoryImpl(
    private val appDatabase: AppDatabase
) : CuratorRepository {
    override fun isEmpty(): Boolean {
        return appDatabase.curatorQueries.isEmpty().executeAsOne()
    }

    override fun add(userId: UserId) {
        appDatabase.curatorQueries.add(userId)
    }

    override fun get(userId: UserId): Curator? {
        return appDatabase.curatorQueries.get(userId).executeAsOneOrNull()?.toDomainModel()
    }

    override fun getCurators(): List<Curator> {
        return appDatabase.curatorQueries.getCurators().executeAsList().map { it.toDomainModel() }
    }

    private fun UserId.toDomainModel() = Curator(
        userId = this
    )
}
