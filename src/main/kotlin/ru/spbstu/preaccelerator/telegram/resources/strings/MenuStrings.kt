package ru.spbstu.preaccelerator.telegram.resources.strings

import ru.spbstu.preaccelerator.domain.entities.Team
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object MenuStrings {
    const val Back = "⬅️ Назад"

    object Curator {
        const val Message = "Меню куратора"

        object SendInfo {
            const val Button = "Разослать информацию"
            const val Message = "Выберите получателей"
            const val ToAll = "Все"
            const val ToTrackers = "Трекеры"
            const val ToSelectTeams = "Выбрать отдельные команды"
        }

        object GetStats {
            const val Button = "Получить статистику"
            const val Message = "Выберите, какую статистику выгрузить"
            const val Teams = "Выгрузить прогресс команд"
            const val Trackers = "Выгрузить прогресс трекеров"
        }

        object AddUsers {
            const val Button = "Дополнить базу пользователей"
            const val Message = "Выберите, кого добавить"
            const val MembersAndTrackers = "Добавить участников и трекеров"
            const val Curator = "Добавить куратора"
        }

        const val GetProtocols = "Выгрузить протоколы встреч"
    }

    object Tracker {
        const val Message = "Меню трекера"

        object Meetings {
            const val Button = "Встречи"
            const val Message = "Выберите действие"
            const val Schedule = "Запланировать новую встречу"
            const val SendProtocol = "Отправить протокол встречи"
        }

        object Teams {
            const val Button = "Команды"
            const val Message = "Выберите действие"
            const val GetHomework = "Выгрузить домашнее задание"
            const val GetStats = "Получить статистику по командам"
        }

        object ScheduleMeetings {
            const val ChooseTeam = "Выберите команду"
            const val InputURL = "Введите ссылку на конференцию"
            const val InputTime = "Введите время конференции в формате дд.ММ.гггг чч:мм"
        }
    }

    private val dateTimeFormatter = DateTimeFormatter
        .ofLocalizedDateTime(FormatStyle.LONG)
        .withZone(ZoneId.systemDefault())

    fun meetingCreationConfirmation (teamName: String, time: OffsetDateTime, url: String) =
        """|Запланировать встречу с командой $teamName
           |на ${dateTimeFormatter.format(time)}
           |ссылка на конференцию $url
           |Все верно?
        """.trimMargin()

}
