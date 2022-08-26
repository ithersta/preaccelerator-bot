package ru.spbstu.preaccelerator.data.adapters

import app.cash.sqldelight.ColumnAdapter
import ru.spbstu.preaccelerator.domain.entities.Homework

object HomeworkIdAdapter : ColumnAdapter<Homework.Id, Long> {
    override fun decode(databaseValue: Long): Homework.Id {
        return Homework.Id(databaseValue)
    }

    override fun encode(value: Homework.Id): Long {
        return value.value
    }
}
