package ru.spbstu.preaccelerator.telegram.resources.strings

import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module
import ru.spbstu.preaccelerator.domain.entities.module.Task
import java.time.OffsetDateTime

object NotificationStrings {
    object ModuleDeadline {
        fun inOneDay(moduleNumber: Module.Number) =
            "Вашей команде необходимо завершить Модуль ${moduleNumber.value} в течение 24 часов"

        fun expired(moduleNumber: Module.Number) =
            "Ваша команда пропустила дедлайн Модуля ${moduleNumber.value}. Завершите его сейчас."
    }

    fun homeworkDownloaded(taskNumb: Task.Number, team: Team) =
        "Домашнее задание №${taskNumb.value} от команды ${team.name} загружено\\!"

    object ProtocolDeadline {
        fun inLessThanTwoDays(moduleNumber: Module.Number) =
            "Осталось менее 2 дней, чтобы заполнить протоколы встреч Модуля ${moduleNumber.value}"
    }

    object MeetingNotifications {
        fun meetingCreatedNotify(dateTime: OffsetDateTime, url: String) =
            """|Ваш трекер запланировал новую встречу
               |Дата встречи: ${MessageStrings.dateTimeFormatter.format(dateTime)}
               |Ссылка на конференцию: $url
               |
               |За 2 часа до назначенного времени вам придёт напоминание.
            """.trimMargin()

    }
}
