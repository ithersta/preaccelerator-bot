package ru.spbstu.preaccelerator.data.adapters

import app.cash.sqldelight.ColumnAdapter
import ru.spbstu.preaccelerator.domain.entities.module.Module

object ModuleNumberAdapter : ColumnAdapter<Module.Number, Int> {
    override fun decode(databaseValue: Int): Module.Number {
        return Module.Number(databaseValue)
    }

    override fun encode(value: Module.Number): Int {
        return value.value
    }
}
