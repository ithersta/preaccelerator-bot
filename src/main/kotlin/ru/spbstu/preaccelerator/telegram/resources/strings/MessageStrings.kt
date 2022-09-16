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
import java.time.format.TextStyle
import java.util.*

// TODO: Всё переписать
object MessageStrings : KoinComponent {
    private val zoneId: ZoneId by inject()
    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter
        .ofLocalizedDateTime(FormatStyle.LONG)
        .withLocale(Locale.forLanguageTag("ru"))
        .withZone(zoneId)

    object Start {
        const val AskContact = "Добро пожаловать в телеграм\\-бот предакселератора НТИ\\.\n" +
                "Чтобы начать путь к успешному стартапу длиной в 8 недель, необходимо поделиться вашим номером телефона\\. " +
                "Это позволит пройти идентификацию\\."
        const val InvalidDeepLink = "Некорректная ссылка или она уже была использована"
        const val NoRoleAssigned = "Твоего номера нет в базе или обучение ещё не началось"
        const val WelcomeCurator = "Добро пожаловать! Вы куратор."
        const val NotStartedSeason = "На данный момент сезон не начался"
        val InputDateTime = "Введите дату и время начала предакселератора в формате дд.ММ.гггг чч:мм (часовой пояс ${
            zoneId.getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("ru"))
        })"
        const val StartSeasonSuccesfully = "Новый поток предакселератора запущен 🎉"
        fun welcomeTracker(teams: List<Team>) = "Добро пожаловать! Вы трекер команд ${teams.joinToString { it.name }}."
        fun welcomeMember(team: Team) = "Добро пожаловать! Ты участник команды ${team.name}."
    }

    object Who {
        const val Empty = "Твоего номера нет в базе"
        const val Curator = "Вы куратор"
        fun tracker(teams: List<Team>) = "Вы трекер команд ${teams.joinToString { it.name }}"
        fun member(team: Team) = "Ты участник команды ${team.name}"
    }

    object SendStatistics {
        const val NoTeams = "Нет команд для сбора статистики"
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
        const val NoHomeworksDone = "Команда ещё не выполнила ни одного задания из этого модуля"

        fun chooseModuleNumber(teamName: String) = "Команда ${teamName}. Выберите номер модуля."
        fun moduleHomeworks(teamName: String, moduleNumber: Module.Number) =
            "Задания модуля №${moduleNumber.value} команды $teamName"
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

        fun protocol(
            prefix: String?,
            protocol: Protocol?,
            team: Team,
            moduleNumber: Module.Number,
            status: String?,
            comment: String?
        ) = buildEntities {
            prefix?.let { regularln(it) }
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

        fun declinedProtocol(moduleNumber: Module.Number, team: Team, protocol: Protocol?, comment: String?) =
            buildEntities {
                if (protocol != null)
                    link("Протокол ${moduleNumber.value} недели с командой ${team.name}", protocol.url)
                regular(" не принят.")
                regularln(" ")
                if (comment != null) {
                    bold("Комментарий куратора: ")
                    regularln(comment)
                }
                regularln(" ")
                regularln("Внесите соответствующие изменения и заново отправьте протокол")
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
        const val MeetingIsCreated =
            "Новая встреча с командой создана. Вы и участники команды получите напоминание о встрече за 2 часа до неё."
        const val MeetingNotCreated = "Встреча не создана"
        const val InvalidModuleNumber = "Введён неверный номер модуля"
        val InputDateTime = "Введите дату и время конференции в формате дд.ММ.гггг чч:мм (часовой пояс ${
            zoneId.getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("ru"))
        })"
        val InvalidDateTime = "Неверный формат даты. $InputDateTime"

        fun meetingCreationConfirmation(teamName: String, dateTime: OffsetDateTime, url: String) =
            """|Запланировать встречу с командой $teamName
           |на ${dateTimeFormatter.format(dateTime)}
           |ссылка на конференцию: $url
           |Всё верно?
        """.trimMargin()
    }

    object SendInfo {
        const val ChooseTeams = "Выберите команды, которым хотите разослать сообщение"
        const val EnterMessage = "Введите текст сообщения"
        const val Started = "Рассылка может занять некоторое время…"
        fun chosenTeams(teams: List<Team>) = "Выбранные команды: ${teams.joinToString { it.name }}"
        fun confirmation(message: String) = """
            |Вы ввели следующее сообщение:
            |$message
            |Разослать его?
        """.trimMargin()

        fun messageFromCurator(message: String) = buildEntities {
            boldln("Сообщение от куратора")
            regular(message)
        }

        fun messageFromTracker(message: String) = buildEntities {
            boldln("Сообщение от трекера")
            regular(message)
        }

        fun success(count: Int) =
            "Сообщение разослано $count ${pluralize(count, "пользователю", "пользователям", "пользователям")}"
    }

    object FillOutProtocol {
        const val InvalidProtocolUrl = "Неверный формат ссылки"
        const val ProtocolHasBeenSent = "Протокол отправлен и находится на проверке"
        const val ChooseTeam = "Выберите команду"
        const val InputGoogleDiskUrl = "Введите ссылку на Google док с протоколом встречи"
        const val MarkAsSentQuestion = "Оповестить куратора о готовности протокола?"
        const val NewProtocol = "Новый протокол отправлен на проверку"

        val ProtocolStatus.Value.emoji
            get() = when (this) {
                ProtocolStatus.Value.Unsent -> "✉️"
                ProtocolStatus.Value.Sent -> "📤"
                ProtocolStatus.Value.Accepted -> "✅"
                ProtocolStatus.Value.Declined -> "❌"
            }

        fun confirmationProtocol(moduleNumber: String) =
            "Вы завершили заполнение протокола $moduleNumber (номер модуля) недели. Куратор будет уведомлён об этом. Вы получите оповещение в случае, если он найдёт недочёты."

        fun explanationReasons(protocolStatus: ProtocolStatus, team: Team, protocol: Protocol) = buildEntities {
            link("Протокол ${protocolStatus.moduleNumber.value} недели с командой ${team.name}", protocol.url)
            bold(" нуждается в изменении\n\nКомментарий куратора:")
            regularln(" ${protocolStatus.comment}\nНажмите на кнопку, если протокол был исправлен.")
        }

        fun chooseModule(teamName: String) = """
            |Протоколы команды $teamName
            |${ProtocolStatus.Value.Unsent.emoji} – протокол ещё не отправлен
            |${ProtocolStatus.Value.Sent.emoji} – протокол отправлен
            |${ProtocolStatus.Value.Accepted.emoji} – протокол принят
            |${ProtocolStatus.Value.Declined.emoji} – протокол отклонён
        """.trimMargin()
    }
}
