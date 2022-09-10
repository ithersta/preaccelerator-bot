package ru.spbstu.preaccelerator.telegram.resources.strings

import ru.spbstu.preaccelerator.domain.entities.module.Module

object NotificationStrings {
    object ModuleDeadline {
        fun inOneDay(moduleNumber: Module.Number) =
            "Вашей команде необходимо завершить Модуль ${moduleNumber.value} в течение 24 часов"

        fun expired(moduleNumber: Module.Number) =
            "Ваша команда пропустила дедлайн Модуля ${moduleNumber.value}. Завершите его сейчас."
    }

    object ProtocolDeadline {
        fun inLessThanTwoDays(moduleNumber: Module.Number) =
            "Осталось менее 2 дней, чтобы заполнить протоколы встреч Модуля ${moduleNumber.value}"
    }
}
