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
        const val NotImplemented = "Эта функция не реализована"
    }

    object Error {
        fun internal(message: String?) = "Произошла внутренняя ошибка: $message"
    }
}
