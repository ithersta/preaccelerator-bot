package ru.spbstu.preaccelerator.telegram.resources.strings

import ru.spbstu.preaccelerator.domain.entities.Meeting
import ru.spbstu.preaccelerator.domain.entities.module.Module

object NotificationStrings {
    object ModuleDeadline {
        fun inOneDay(moduleNumber: Module.Number) = "Вашей команде необходимо завершить Модуль ${moduleNumber.value + 1} в течение 24 часов"
        fun expired(moduleNumber: Module.Number) = "Ваша команда пропустила дедлайн Модуля ${moduleNumber.value + 1}. Завершите его сейчас."
    }

    object ProtocolDeadline {
        fun inTwoDays(meetings: List<Meeting>) = """
            |Вам необходимо заполнить протоколы следующих встреч в течение 2 дней:
            |${meetings.joinToString(separator = "\n") { it.id.toString() }}
        """.trimMargin()
    }
}
