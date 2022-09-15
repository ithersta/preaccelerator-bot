package ru.spbstu.preaccelerator.telegram.resources.strings

import org.koin.core.component.KoinComponent
import ru.spbstu.preaccelerator.domain.entities.module.Module

object SpreadsheetStrings : KoinComponent {
    object StatisticsTable {
        const val TeamName = "Название команды"
        const val DateOfCompletion = "Дата сдачи домашнего задания"
        const val TotalCompleted = "Всего выполнили"
        const val FileName = "Сводка команды"
        fun moduleString(number: Module.Number) = "Модуль ${number.value}"
    }
}
