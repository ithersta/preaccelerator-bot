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

    object LoadListOfUsers{
        const val LoadList = "загрузите xlsx таблицу со списками пользователей"
        const val InvalidFile = "Файл поврежден или не является xlsx таблицей"
        const val BadFormatMembers = "Неправильный формат таблицы участников в строках: "
        const val BadFormatTrackers = "Неправильный формат таблицы команд в строках: "
        const val OkAddMembers = "Добавлено участников курсов: "
        const val OkAddTeams = "Добавлено команд курсов: "
        const val NotFindTeam = "Не найдены трекеры для команд(ы), поэтому участники записанные в них не добавлены: "
    }

    object Error {
        fun internal(message: String?) = "Произошла внутренняя ошибка: $message"
    }
}
