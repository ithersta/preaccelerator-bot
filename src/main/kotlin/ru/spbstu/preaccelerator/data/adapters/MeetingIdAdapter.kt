package ru.spbstu.preaccelerator.data.adapters

import app.cash.sqldelight.ColumnAdapter
import ru.spbstu.preaccelerator.domain.entities.Meeting

object MeetingIdAdapter : ColumnAdapter<Meeting.Id, Long> {
    override fun decode(databaseValue: Long): Meeting.Id {
        return Meeting.Id(databaseValue)
    }

    override fun encode(value: Meeting.Id): Long {
        return value.value
    }
}
