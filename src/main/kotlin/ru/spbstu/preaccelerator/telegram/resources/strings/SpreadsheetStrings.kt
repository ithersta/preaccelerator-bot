package ru.spbstu.preaccelerator.telegram.resources.strings

import ru.spbstu.preaccelerator.domain.entities.module.Module
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.SignStyle
import java.time.temporal.ChronoField

object SpreadsheetStrings {
    object StatisticsTable {
        const val TeamName = "Название команды"
        const val DateOfCompletion = "Дата сдачи домашнего задания"
        const val TotalCompleted = "Всего выполнили"
        val OffsetDateTimeFormatter = DateTimeFormatter.ofPattern("uuuu.MM.dd;kk:mmx")

        fun moduleString(number: Module.Number) = "Модуль ${number.value}"
        const val FileName = "Сводка команды"
    }
}