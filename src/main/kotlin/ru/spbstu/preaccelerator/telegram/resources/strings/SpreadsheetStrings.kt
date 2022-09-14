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
        val OffsetDateTimeFormatter = DateTimeFormatterBuilder()
            .appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
            .appendLiteral('.')
            .appendValue(ChronoField.MONTH_OF_YEAR, 2)
            .appendLiteral('.')
            .appendValue(ChronoField.DAY_OF_MONTH, 2)
            .appendLiteral('T')
            .appendValue(ChronoField.HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .optionalStart()
            .appendLiteral(':')
            .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
            .optionalStart()
            .toFormatter()

        fun moduleString(number: Module.Number) = "Модуль ${number.value}"
        const val FileName = "Сводка команды"
    }
}