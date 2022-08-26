package ru.spbstu.preaccelerator.data.adapters

import app.cash.sqldelight.ColumnAdapter
import ru.spbstu.preaccelerator.domain.entities.user.Member

object MemberIdAdapter : ColumnAdapter<Member.Id, Long> {
    override fun decode(databaseValue: Long): Member.Id {
        return Member.Id(databaseValue)
    }

    override fun encode(value: Member.Id): Long {
        return value.value
    }
}
