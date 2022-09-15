package ru.spbstu.preaccelerator.telegram.resources.strings

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.module.Module
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object SpreadsheetStrings : KoinComponent {
    object StatisticsTable {
        const val TeamName = "Название команды"
        const val DateOfCompletion = "Дата сдачи домашнего задания"
        const val TotalCompleted = "Всего выполнили"
        const val FileName = "Сводка команды"
        fun moduleString(number: Module.Number) = "Модуль ${number.value}"
    }
}
