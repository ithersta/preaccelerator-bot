package ru.spbstu.preaccelerator.telegram.resources.strings

import ru.spbstu.preaccelerator.domain.entities.module.Module

object NotificationStrings {
    object ModuleDeadline {
        fun inOneDay(moduleNumber: Module.Number) =
            "Вашей команде необходимо завершить Модуль ${moduleNumber.value + 1} в течение 24 часов"

        fun expired(moduleNumber: Module.Number) =
            "Ваша команда пропустила дедлайн Модуля ${moduleNumber.value + 1}. Завершите его сейчас."
    }

    object ProtocolDeadline {
        fun inLessThanTwoDays(moduleNumber: Module.Number) =
            "Осталось менее 2 дней, чтобы заполнить протоколы встреч Модуля ${moduleNumber.value + 1}"
    }

    object MassSendInfo {
        const val inputOneMessage = "Введите текст сообщения"
        const val listOfTeams = "Выберите команды для рассылки:"
        fun requestConfirm(message: String) = "Вы ввели сообщение: $message;\nЧтобы заменить сообщение, введите его"
        const val send = "Отправить"
        const val inputOtherMessage = "Ввести другое сообщение"
        const val sendSuchTeams = "Отправить выбранным командам"
        const val startSendInfo = "Началась рассылка информации"
        const val finishSendInfo = "Рассылка закончилась"
        fun notificationCurator(message: String) = "Куратор сообщает:\n$message"
        fun notificationTracker(message: String) = "Трекер сообщает:\n$message"
        fun listTeams(mutableSetTeamId: MutableSet<Long>, mapOfTeams: Map<Long, String>) =
            "Сообщение будет разослано командам: ${
                mutableSetTeamId.joinToString { mapOfTeams[it]!! }
            }"
    }
}
