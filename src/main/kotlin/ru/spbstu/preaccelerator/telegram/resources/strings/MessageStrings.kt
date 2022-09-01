package ru.spbstu.preaccelerator.telegram.resources.strings

import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.user.Tracker

// TODO: Всё переписать
object MessageStrings {
    object Start {
        const val AskContact = "TODO"
        const val NoRoleAssigned = "Твоего номера нет в базе или обучение ещё не началось"
        const val WelcomeCurator = "Добро пожаловать! Вы куратор."
        fun welcomeTracker(id: Tracker.Id) = "Добро пожаловать! Вы трекер #${id.value}."
        fun welcomeMember(team: Team) = "Добро пожаловать! Ты участник команды ${team.name}."
    }

    object Who {
        const val Empty = "Твоего номера нет в базе"
        const val Curator = "Вы куратор"
        fun tracker(id: Tracker.Id) = "Вы трекер #${id.value}"
        fun member(team: Team) = "Ты участник команды ${team.name}"
    }

    object Cancel {
        const val Success = "Операция отменена"
        const val NothingToCancel = "Нечего отменять"
    }

    object Fallback {
        const val NoSuchCommand = "Нет такой команды или она сейчас недоступна"
    }

    object LoadListOfUsers {
        const val LoadList = "загрузите xlsx таблицу со списками пользователей"
        const val InvalidFile = "Файл поврежден или не является xlsx таблицей"
        fun badFormat(members: List<Int>?, teams: List<Int>?): String {
            return if (members != null) {
                "Неправильный формат таблицы участников в строках: " + if (members.size <= 5) {
                    members.map { it.toString() }.joinToString { it }
                } else {
                    members.subList(0, 5).map { it.toString() }
                        .joinToString { it } + " и так далее. \n"
                }
            } else {
                ""
            } + if (teams != null) {
                "Неправильный формат таблицы команд в строках: " + if (teams.size <= 5) {
                    teams.map { it.toString() }.joinToString { it }
                } else {
                    teams.subList(0, 5).map { it.toString() }
                        .joinToString { it } + " и так далее."
                }
            } else {
                ""
            }
        }
        const val OkAddMembers = "Добавлено участников курсов: "
        const val OkAddTeams = "Добавлено команд курсов: "
        const val NotFindTeam = "Не найдены трекеры для команд(ы), поэтому участники записанные в них не добавлены: "
    }

    object Error {
        fun internal(message: String?) = "Произошла внутренняя ошибка: $message"
    }
}
