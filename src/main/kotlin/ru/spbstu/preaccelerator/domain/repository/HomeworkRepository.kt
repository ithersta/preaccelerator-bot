package ru.spbstu.preaccelerator.domain.repository

import ru.spbstu.preaccelerator.domain.entities.Homework
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Task
import java.time.OffsetDateTime

interface HomeworkRepository {
    fun get(teamId: Team.Id): List<Homework>
    fun get(teamId: Team.Id, taskNumber: Task.Number): Homework?
    fun add(teamId: Team.Id, taskNumber: Task.Number, url: String, timestamp: OffsetDateTime): Boolean
}
