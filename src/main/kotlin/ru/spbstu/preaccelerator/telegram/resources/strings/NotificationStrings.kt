package ru.spbstu.preaccelerator.telegram.resources.strings

import dev.inmo.tgbotapi.utils.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.Meeting
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module
import ru.spbstu.preaccelerator.domain.entities.module.Task
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

object NotificationStrings : KoinComponent {
    private val zoneId: ZoneId by inject()

    object ModuleDeadline {
        fun inOneDay(moduleNumber: Module.Number) =
            "Вашей команде необходимо завершить Модуль ${moduleNumber.value} в течение 24 часов"

        fun expired(moduleNumber: Module.Number) =
            "Ваша команда пропустила дедлайн Модуля ${moduleNumber.value}. Завершите его сейчас."
    }

    object MeetingStart {
        private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm v")
            .withZone(zoneId)
            .withLocale(Locale.forLanguageTag("ru"))

        fun inTwoHoursForTeam(meeting: Meeting) = buildEntities {
            regular("Через ")
            bold("2 часа")
            regularln(" (в ${timeFormatter.format(meeting.timestamp)}) у вашей команды состоится встреча с трекером")
            bold("Ссылка на конференцию: ")
            regularln(meeting.url)
        }

        fun inTwoHoursForTracker(meeting: Meeting, team: Team) = buildEntities {
            regular("Через ")
            bold("2 часа")
            regularln(" (в ${timeFormatter.format(meeting.timestamp)}) у вас состоится встреча с командой ${team.name}")
            bold("Ссылка на конференцию: ")
            regularln(meeting.url)
        }
    }

    fun homeworkUploaded(task: Task, team: Team) =
        "Домашнее задание №${task.number.value} по Модулю ${task.moduleNumber.value} от команды ${team.name} загружено!"

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
                regularln("За 2 часа до назначенного времени вам придёт сообщение")
            }

        fun meetingCreatedNotifyCurator(dateTime: OffsetDateTime, url: String, teamName: String) =
            buildEntities {
                regularln("Запланирована новая встреча для команды $teamName")
                regularln("")
                bold("Дата встречи: ")
                regularln(MessageStrings.dateTimeFormatter.format(dateTime))
                bold("Ссылка на конференцию: ")
                linkln(url)
            }
    }
}
