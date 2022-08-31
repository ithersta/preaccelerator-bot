package ru.spbstu.preaccelerator.telegram.resources.strings

import kotlinx.serialization.Serializable
import ru.spbstu.preaccelerator.telegram.entities.state.DialogState

object MenuStrings {
    const val Back = "Назад"

    object Curator {
        const val Message = "Меню куратора"

        object SendInfo {
            const val Button = "Разослать информацию"
            const val Message = "Выберите получателей"
            const val ToAll = "Все"
            const val ToTrackers = "Треккеры"
            const val ToSelectTeams = "Выбрать отдельные команды"
        }

        object GetStats {
            const val Button = "Получить статистику"
            const val Message = "Выберите, какую статистику выгрузить"
            const val Teams = "Выгрузить прогресс команд"
            const val Trackers = "Выгрузить прогресс треккеров"
        }

        object AddUsers {
            const val Button = "Дополнить базу пользователей"
            const val Message = "Выберите, кого добавить"
            const val MembersAndTrackers = "Добавить участников и треккеров"
            const val Curator = "Добавить куратора"
        }

        const val GetProtocols = "Выгрузить протоколы встреч"
    }

    object Tracker {
        const val Message = "Меню треккера"

        object Meetings {
            const val Button = "Встречи"
            const val Message = "Выберите действие"
            const val Schedule = "Запланировать новую встречу"
            const val SendProtocol = "Отправить протокол встречи"
        }

        object Teams {
            const val Button = "Команды"
            const val Message = "Выберите действие"
            const val GetHomework = "Выгрузить домашнее задание"
            const val GetStats = "Получить статистику по командам"
        }
    }

    object Member {
        const val Message = "Меню участника"
        const val SelectModule = "Выбрать модуль"
        const val GetRating = "Получить место в рейтинге"
    }
}
