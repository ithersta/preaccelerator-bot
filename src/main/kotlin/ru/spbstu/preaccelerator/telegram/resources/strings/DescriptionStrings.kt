package ru.spbstu.preaccelerator.telegram.resources.strings

import com.ithersta.tgbotapi.menu.entities.Menu
import ru.spbstu.preaccelerator.domain.entities.user.PreacceleratorUser
import ru.spbstu.preaccelerator.telegram.entities.state.DialogState

object DescriptionStrings {

    enum class Roles {
        Users,
        Trackers,
        Teams,
        Members,
        Curators
    }

    private val DativeCase = mapOf(
        Roles.Users to "всем",
        Roles.Trackers to "трекерам",
        Roles.Teams to "командам"
    )
    private val GenitiveCase = mapOf(
        Roles.Members to "участников",
        Roles.Curators to "кураторов",
        Roles.Trackers to "трекеров",
    )

    fun sendInfo(recipient: Roles): String = "отправить информацию ${DativeCase.getValue(recipient)}"
    fun getStats(category: Roles) = "получить статистику по ${DativeCase.getValue(category)}"

    object CuratorButtons {
        const val GetProtocol = "получать и проверять протоколы команд"
        fun addUsers(users: List<Roles>) =
            "добавлять новых ${users.map { GenitiveCase.getValue(it) }}"
    }

    object TrackerButtons {
        const val ScheduleMeeting = "планировать встречи с командой"
        const val SendProtocol = "отправлять протоколы встреч на проверку"
        const val DownloadHomework = "загружать домашние задания команд"
    }

    object MemberButtons {
        const val GetProtocol = "получать протокол встреч"
        const val DoModules = "проходить модули целиком, получать итоговые тесты, смотреть лекции"
    }

    const val PossibleActions = "Функционал бота позволяет вам:\n"

    fun functionalDescription(menu: Menu<DialogState, PreacceleratorUser, out PreacceleratorUser>): String {
        var description = PossibleActions
        for (action in menu.descriptions) {
            description += "• " + action + "\n"
        }
        return description
    }
}
