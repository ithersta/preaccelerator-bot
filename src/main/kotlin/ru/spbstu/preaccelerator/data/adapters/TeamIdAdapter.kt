package ru.spbstu.preaccelerator.data.adapters

import app.cash.sqldelight.ColumnAdapter
import ru.spbstu.preaccelerator.domain.entities.Team

object TeamIdAdapter : ColumnAdapter<Team.Id, Long> {
    override fun decode(databaseValue: Long): Team.Id {
        return Team.Id(databaseValue)
    }

    override fun encode(value: Team.Id): Long {
        return value.value
    }
}
