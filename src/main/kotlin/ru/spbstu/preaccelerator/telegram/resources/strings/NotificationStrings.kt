package ru.spbstu.preaccelerator.telegram.resources.strings

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.Meeting
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module
import ru.spbstu.preaccelerator.domain.entities.module.Task
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

object NotificationStrings : KoinComponent {
    private val zoneId: ZoneId by inject()
    private val dateTimeFormatter = DateTimeFormatter
        .ofLocalizedDateTime(FormatStyle.LONG)
        .withLocale(Locale.forLanguageTag("ru"))
        .withZone(zoneId)

    object ModuleDeadline {
        fun inOneDay(moduleNumber: Module.Number) =
            "Вашей команде необходимо завершить Модуль ${moduleNumber.value} в течение 24 часов"

        fun expired(moduleNumber: Module.Number) =
            "Ваша команда пропустила дедлайн Модуля ${moduleNumber.value}. Завершите его сейчас."
    }

    object MeetingStart {
        fun inTwoHoursForTeam(meeting: Meeting) = """
            |Через 2 часа у вашей команды состоится встреча с трекером
            |${dateTimeFormatter.format(meeting.timestamp)}
            |${meeting.url}
        """.trimMargin()

        fun inTwoHoursForTracker(meeting: Meeting, team: Team) = """
            |Через 2 часа у вас состоится встреча с командой ${team.name}
            |${dateTimeFormatter.format(meeting.timestamp)}
            |${meeting.url}
        """.trimMargin()
    }

    fun homeworkUploaded(task: Task, team: Team) =
        "Домашнее задание №${task.number.value} по Модулю ${task.moduleNumber.value} от команды ${team.name} загружено!"

    object ProtocolDeadline {
        fun inLessThanTwoDays(moduleNumber: Module.Number) =
            "Осталось менее 2 дней, чтобы заполнить протоколы встреч Модуля ${moduleNumber.value}"
    }
}
