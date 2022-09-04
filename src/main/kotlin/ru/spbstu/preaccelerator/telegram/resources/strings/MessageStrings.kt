package ru.spbstu.preaccelerator.telegram.resources.strings

import ru.spbstu.preaccelerator.domain.entities.Team

// TODO: Всё переписать
object MessageStrings {
    object Start {
        const val AskContact = "TODO"
        const val NoRoleAssigned = "Твоего номера нет в базе или обучение ещё не началось"
        const val WelcomeCurator = "Добро пожаловать! Вы куратор."
        fun welcomeTracker(teams: List<Team>) = "Добро пожаловать! Вы трекер команд ${teams.joinToString { it.name }}"
        fun welcomeMember(team: Team) = "Добро пожаловать! Ты участник команды ${team.name}."
    }

    object Who {
        const val Empty = "Твоего номера нет в базе"
        const val Curator = "Вы куратор"
        fun tracker(teams: List<Team>) = "Вы трекер команд ${teams.joinToString { it.name }}"
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

    object LoadListOfUsers {
        const val WaitDocument = "Заполните шаблон и прикрепите ответным сообщением"
        const val InvalidFile = "Файл повреждён или не является xlsx таблицей"
        fun badFormat(members: List<Int>?, teams: List<Int>?) = buildString {
            if (members != null) {
                appendLine("Неправильный формат таблицы участников в строках: ${enumerationOfLimitedList(members, 5)}")
            }
            if (teams != null) {
                appendLine("Неправильный формат таблицы команд в строках: ${enumerationOfLimitedList(teams, 5)}")
            }
        }

        fun success(membersCount: Int, teamsCount: Int) = "Добавлены $membersCount ${
            pluralize(membersCount, "участник", "участника", "участников")
        } и $teamsCount ${
            pluralize(teamsCount, "команда", "команды", "команд")
        }"

        fun notFoundTeams(teams: List<String>) = "Следующие команды не найдены: ${teams.joinToString()}"
    }

    object Error {
        fun internal(message: String?) = "Произошла внутренняя ошибка: $message"
    }
}
