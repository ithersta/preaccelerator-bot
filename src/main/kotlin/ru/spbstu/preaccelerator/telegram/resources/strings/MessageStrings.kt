package ru.spbstu.preaccelerator.telegram.resources.strings

import dev.inmo.tgbotapi.extensions.utils.formatting.*
import ru.spbstu.preaccelerator.domain.entities.Protocol
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module
import ru.spbstu.preaccelerator.domain.usecases.AddUsersUseCase
import ru.spbstu.preaccelerator.telegram.parsers.Xlsx

// TODO: Всё переписать
object MessageStrings {
    object Start {
        const val AskContact = "TODO"
        const val InvalidDeepLink = "Некорректная ссылка или она уже была использована"
        const val NoRoleAssigned = "Твоего номера нет в базе или обучение ещё не началось"
        const val WelcomeCurator = "Добро пожаловать! Вы куратор."
        fun welcomeTracker(teams: List<Team>) = "Добро пожаловать! Вы трекер команд ${teams.joinToString { it.name }}."
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

    object ChooseModuleAction {
        const val ChooseModuleAction = "Выберите желаемое действие"
        const val ModuleLectures = "Лекции этого модуля\\. Для просмотра нажмите на интересующую лекцию"
        const val Err = "Выберите действие из кнопочного меню"
    }

    object AddUsers {
        const val WaitDocument = "Заполните шаблон и прикрепите ответным сообщением"
        const val TemplateFilename = "Шаблон"
        const val InvalidFile = "Файл повреждён или не является .xlsx таблицей"
        fun badFormat(errors: List<Xlsx.SheetErrors>) = errors.joinToString(separator = "\n") { tableErrors ->
            "Лист \"${tableErrors.name}\": неправильный формат ${
                if (tableErrors.rows.size == 1) "строки" else "строк"
            } ${enumerationOfLimitedList(tableErrors.rows, 5)}"
        }

        fun result(result: AddUsersUseCase.Result) = buildString {
            if (result.notFoundTeams.isNotEmpty()) {
                appendLine(
                    "Некоторые участники были пропущены, так как не ${
                        if (result.notFoundTeams.size == 1) "найдена команда" else "найдены команды"
                    } ${result.notFoundTeams.joinToString()}."
                )
            }
            appendLine(
                "Добавлены ${result.membersCount} ${
                    pluralize(result.membersCount, "участник", "участника", "участников")
                } и ${result.teamsCount} ${
                    pluralize(result.teamsCount, "команда", "команды", "команд")
                }"
            )
        }
    }

    object ReviewProtocols {
        const val Accepted = "✅"
        const val Declined = "❌"

        const val SendComment = "Напишите в комментарии причину"
        const val SendCommentPlaceholder = "Комментарий"
        const val NoUnreviewedProtocols = "🎉 Непроверенных протоколов больше нет!"

        fun protocol(protocol: Protocol?, team: Team, moduleNumber: Module.Number, status: String?, comment: String?) =
            buildEntities {
                if (status != null) {
                    regular(status)
                    regular(" ")
                }
                bold("Модуль ${moduleNumber.value + 1}")
                regular(" | ")
                bold("Команда: ")
                regularln(team.name)
                if (protocol != null) {
                    bold("Протокол: ")
                    linkln(protocol.url)
                } else {
                    regularln("Ссылка на протокол не установлена")
                }
                if (comment != null) {
                    bold("Комментарий: ")
                    regularln(comment)
                }
            }
    }

    object Error {
        fun internal(message: String?) = "Произошла внутренняя ошибка: $message"
    }

    object Curator {
        fun addCuratorDeepLink(deepLink: String) = "Отправьте одноразовую ссылку будущему куратору: $deepLink"
    }
}
