package ru.spbstu.preaccelerator.data.repository

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.data.AppDatabase
import ru.spbstu.preaccelerator.domain.entities.Homework
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Task
import ru.spbstu.preaccelerator.domain.repository.HomeworkRepository
import java.time.OffsetDateTime

@Single
class HomeworkRepositoryImpl(
    private val appDatabase: AppDatabase
) : HomeworkRepository {
    override fun get(teamId: Team.Id): List<Homework> {
        return appDatabase.homeworkQueries.getByTeamId(teamId).executeAsList().map { it.toDomainModel() }
    }

    override fun get(teamId: Team.Id, taskNumber: Task.Number): Homework? {
        return appDatabase.homeworkQueries.get(teamId, taskNumber).executeAsOneOrNull()?.toDomainModel()
    }

    override fun getAll(): List<Homework> {
        return appDatabase.homeworkQueries.getAll().executeAsList().map { it.toDomainModel() }
    }

    override fun add(teamId: Team.Id, taskNumber: Task.Number, url: String, timestamp: OffsetDateTime): Boolean {
        return appDatabase.homeworkQueries.add(teamId, taskNumber, url, timestamp).executeAsOneOrNull() != null
    }

    private fun ru.spbstu.preaccelerator.data.Homework.toDomainModel() = Homework(
        teamId = teamId,
        taskNumber = taskNumber,
        url = url,
        timestamp = timestamp
    )
}
