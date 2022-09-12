package ru.spbstu.preaccelerator.telegram.resources.strings

import java.time.OffsetDateTime
import java.time.ZoneOffset

object SpreadsheetStrings {
    object StatisticsTable {
        const val NameTeam = "Название команды"
        const val DateOfComplete = "Дата сдачи домашнего задания"

        data class Module(
            val name: String,
            val range: Int
        )

        val modules = listOf(
            Module("Модуль 1", 4),
            Module("Модуль 2", 4),
            Module("Модуль 3", 2),
            Module("Модуль 4", 3),
            Module("Модуль 5", 1),
            Module("Модуль 6", 1),
            Module("Модуль 7", 1),
            Module("Модуль 8", 1)
        )
        const val TotalCompleted = "Всего выполнили"
        fun timeStampToString(dateTime: OffsetDateTime) = String.format(
            "%04d.%02d.%02dT%02d:%02d",
            dateTime.year,
            dateTime.month.value,
            dateTime.dayOfMonth,
            dateTime.hour,
            dateTime.minute
        )


        fun formula(column: Char, count: Int) = "COUNTA(${column}4:${column}${count + 3})"
        const val FileName = "Сводка команды"
    }
}