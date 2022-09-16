package ru.spbstu.preaccelerator.telegram.resources.strings

import ru.spbstu.preaccelerator.domain.entities.module.Task

object ButtonStrings {

    object Start {
        const val SendContact = "Отправить мой номер телефона"
    }

    object Module {
        const val WatchLecture = "Запись лекции"
        const val ShowPresentation = "Презентация"
        const val NextPart = "Продолжить обучение"
        const val DoTest = "Пройти тест"
        const val HomeworkAlreadyDone = "Уже отправлено другим участником"
        const val SendHomework = "Отправить домашнее задание"
    }

    object Homework {
        const val SeeHomework = "Посмотреть домашнее задание"

        object ModuleSpecific {
            const val LastOnly = "Задания последнего модуля"
            const val AllMod = "Выбор из всех"
        }

        fun taskNumber(numb: Task.Number) =
            "Задание №${numb.value}"
    }

    object ChooseModule {
        const val Button = "Выбрать модуль"
        const val DoEntireModule = "Пройти модуль полностью"
        const val DoTest = "Пройти тест"
        const val WatchLectures = "Смотреть лекции"
    }

    object ReviewProtocols {
        const val Accept = "✅ Принять"
        const val Decline = "❌ Отклонить"
        const val All = "⏭️ Все непроверенные протоколы"
    }

    object SendInfo {
        const val FinishChoosingTeams = "Отправить выбранным командам"
        const val Confirm = "Разослать"
        const val ChangeMessage = "Ввести другое сообщение"
    }

    object FillOutProtocol {
        const val Fixed = "Протокол исправлен"
        const val MarkAsSent = "Оповестить"
    }

    object Option {
        const val Yes = "Да"
        const val No = "Нет"
    }

    const val Cancel = "Отменить"

    fun selected(text: String) = "✅ $text"
    fun unselected(text: String) = text
}
