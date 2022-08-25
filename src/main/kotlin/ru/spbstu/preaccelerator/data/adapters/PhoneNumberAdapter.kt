package ru.spbstu.preaccelerator.data.adapters

import app.cash.sqldelight.ColumnAdapter
import ru.spbstu.preaccelerator.domain.entities.PhoneNumber

object PhoneNumberAdapter : ColumnAdapter<PhoneNumber, String> {
    override fun decode(databaseValue: String): PhoneNumber {
        return PhoneNumber.of(databaseValue)!!
    }

    override fun encode(value: PhoneNumber): String {
        return value.value
    }
}
