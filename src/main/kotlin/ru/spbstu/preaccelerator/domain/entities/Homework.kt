package ru.spbstu.preaccelerator.domain.entities

import ru.spbstu.preaccelerator.domain.entities.module.Task
import java.time.OffsetDateTime

class Homework(
    val teamId: Team.Id,
    val taskNumber: Task.Number,
    val url: String,
    val timestamp: OffsetDateTime
)
