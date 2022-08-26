package ru.spbstu.preaccelerator.data.adapters

import app.cash.sqldelight.ColumnAdapter
import ru.spbstu.preaccelerator.domain.entities.user.Tracker

object TrackerIdAdapter : ColumnAdapter<Tracker.Id, Long> {
    override fun decode(databaseValue: Long): Tracker.Id {
        return Tracker.Id(databaseValue)
    }

    override fun encode(value: Tracker.Id): Long {
        return value.value
    }
}
