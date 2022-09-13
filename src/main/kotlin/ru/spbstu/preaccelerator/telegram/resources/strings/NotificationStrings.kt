package ru.spbstu.preaccelerator.telegram.resources.strings

import dev.inmo.tgbotapi.extensions.utils.formatting.*
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
        fun meetingCreatedNotifyMember(dateTime: OffsetDateTime, url: String) =
            buildEntities {
                regularln("Ваш трекер запланировал новую встречу")
                bold("Дата встречи: ")
                regularln(MessageStrings.dateTimeFormatter.format(dateTime))
                bold("Ссылка на конференцию: ")
                linkln(url)
                regularln("")
                regularln("За 2 часа до назначенного времени вам придёт напоминание")
            }

        fun meetingCreatedNotifyCurator(dateTime: OffsetDateTime, url: String, teamName: String) =
            buildEntities {
                regularln("Трекер запланировал новую встречу для команды $teamName")
                regularln("")
                bold("Дата встречи: ")
                regularln(MessageStrings.dateTimeFormatter.format(dateTime))
                bold("Ссылка на конференцию: ")
                linkln(url)
            }
    }
}

