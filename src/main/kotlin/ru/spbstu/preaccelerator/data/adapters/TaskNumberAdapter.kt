package ru.spbstu.preaccelerator.data.adapters

import app.cash.sqldelight.ColumnAdapter
import ru.spbstu.preaccelerator.domain.entities.module.Task

object TaskNumberAdapter : ColumnAdapter<Task.Number, Int> {
    override fun decode(databaseValue: Int): Task.Number {
        return Task.Number(databaseValue)
    }

    override fun encode(value: Task.Number): Int {
        return value.value
    }
}
