package ru.spbstu.preaccelerator.telegram.resources.strings

import dev.inmo.tgbotapi.extensions.utils.formatting.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.Protocol
import ru.spbstu.preaccelerator.domain.entities.ProtocolStatus
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module
import ru.spbstu.preaccelerator.domain.usecases.AddUsersUseCase
import ru.spbstu.preaccelerator.telegram.parsers.Xlsx
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

// TODO: Всё переписать
object MessageStrings : KoinComponent {
    private val zoneId: ZoneId by inject()

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

    object DownloadHomework {
        const val ChooseTeam = "Выберите команду"
        const val DownloadOption = "Выберите модуль, к которому относится задание"
        const val Err = "Выберите вариант из кнопочного меню"
        fun moduleHomeworks(num: Module.Number) = "Задания модуля №${num.value}"

        const val NoHomeworksDone = "Команда ещё не выполнила ни одного задания из этого модуля"
        const val ChooseModuleNumber = "Выберите номер модуля"
    }

    object GetProtocol {
        fun teamProtocol(team: Team, protocol: Protocol) = buildEntities {
            link("Протокол команды ${team.name}", protocol.url)
        }

        val NoProtocol = buildEntities {
            regular("Протокол ещё не закреплён за командой. Обратитесь к своему трекеру, чтобы он отправил его на проверку.")
        }
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
        const val NoMoreUnreviewedProtocols = "🎉 Непроверенных протоколов больше нет!"
        const val NoUnreviewedProtocols = "Непроверенных протоколов нет"
        const val ChooseTeam = "Выберите команду, у которой хотите проверить протоколы"

        fun protocol(protocol: Protocol?, team: Team, moduleNumber: Module.Number, status: String?, comment: String?) =
            buildEntities {
                if (status != null) {
                    regular(status)
                    regular(" ")
                }
                bold("Модуль ${moduleNumber.value}")
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

    object ScheduleMeetings {
        const val InputModuleNumber = "Укажите номер модуля, соответствующий теме встречи"
        const val ChooseTeam = "Выберите команду"
        const val InputUrl = "Введите ссылку на конференцию"
        const val InputDateTime = "Введите дату и время конференции в формате дд\\.ММ\\.гггг чч:мм"
        const val MeetingIsCreated =
            "Новая встреча с командой создана\\. Вы и участники команды получите напоминание о встрече за 2 часа до неё"

        //TODO написать красиво
        const val MeetingNotCreated = "Встреча не создана"
        const val InvalidDataFormat = "Введён неверный формат данных"
        const val InvalidModuleNumber = "Введён неверный номер модуля"
    }

    private val dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG).withZone(zoneId)

    fun meetingCreationConfirmation(teamName: String, dateTime: OffsetDateTime, url: String) =
        """|Запланировать встречу с командой $teamName
           |на ${dateTimeFormatter.format(dateTime)}
           |ссылка на конференцию: $url
           |Всё верно?
        """.trimMargin()


    object Tracker {
        val map = mapOf(ProtocolStatus.Value.Unsent to "✉️",
            ProtocolStatus.Value.Declined   to "❌",
            ProtocolStatus.Value.Sent to "\uD83D\uDCE4",
            ProtocolStatus.Value.Accepted to "✅" )
        fun confirmationProtocol(moduleNumber: String) =
            "Вы завершили заполнение протокола $moduleNumber (номер модуля) недели. Куратор будет уведомлён об этом. Вы получите оповещение случае, если он найдёт недочёты."

        fun textForCurator(countOfWeek: String, teamName: String) =
            "Протокол $countOfWeek недели  от команды $teamName отправлен на проверку."

        fun explanationReasons(countOfWeek: String, reason: String) =
            "Куратор не принял протокол недели $countOfWeek\nПричина: $reason"

        const val ProtocolHasBeenSent = "Протокол отправлен и находится на проверке"
        const val ChooseTeam = "Выберите команду"
        const val ProtocolChanged = "Протокол исправлен"
        const val ChooseModule = "Выберите модуль"
        const val InputGoogleDiskUrl = "Введите ссылку на Google док с протоколом встречи"
        const val MessageCurator = "Оповестить куратора о готовности протокола"
        const val Attention = "Нажмите на кнопку, чтобы отправить протокол"
        const val ViewProtocol = "Посмотреть протокол"
        const val ReadyCheck = "Готовы оценить протокол сейчас?"
    }
}
