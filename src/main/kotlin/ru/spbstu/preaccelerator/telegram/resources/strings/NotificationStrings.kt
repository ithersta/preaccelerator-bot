package ru.spbstu.preaccelerator.telegram.resources.strings

import ru.spbstu.preaccelerator.domain.entities.module.Module

object NotificationStrings {
    object ModuleDeadline {
        fun inOneDay(moduleNumber: Module.Number) = "Вашей команде необходимо завершить Модуль ${moduleNumber.value + 1} в течение 24 часов"
    }
}
