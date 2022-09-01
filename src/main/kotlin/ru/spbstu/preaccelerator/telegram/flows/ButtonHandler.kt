package ru.spbstu.preaccelerator.telegram.flows

import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.ReplyKeyboardMarkup
import dev.inmo.tgbotapi.types.buttons.SimpleKeyboardButton
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.telegram.StateMachineBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings

private fun arrayOfSimpleKeyboardButtonsForArray(arr: Array<String>): Array<SimpleKeyboardButton> {
    return arr.map { SimpleKeyboardButton(it) }.toTypedArray()
}
// INT  - НОМЕР МОДУЛЯ  STRING - ИМЯ МОДУЛЯ
private fun arrayOfSimpleKeyboardButtons(map: Map<Int, String>): Array<SimpleKeyboardButton> {
    return map.map { SimpleKeyboardButton(it.value) }.toTypedArray()
}
//TODO:
/*
    ЕСЛИ КОМАНДА СДЕЛАЛА ДОМАШКУ ВОВРЕМЯ  ++ ТО ВСЕ УЧАСТНИКИ ПОЛУЧАЮТ ++ К НОМЕРУ МОДУЛЯ.
    ПРЕДЛАГАЕТСЯ ПРОЙТИ ИТОГОВЫЙ ТЕСТ (ГУГЛ ФОРМА).
    ЖДУТ ПОКА ОТКРОЕТСЯ НОВЫЙ МОДУЛЬ.
*/

fun StateMachineBuilder.button() {
    role<Tracker> {
        state<EmptyState> {
            onTransition {
                refreshCommands()
                sendTextMessage(
                    it, ButtonStrings.ChooseStep,
                    replyMarkup = ReplyKeyboardMarkup(
                        buttons = arrayOfSimpleKeyboardButtonsForArray(ButtonStrings.listOfModels.toTypedArray()),
                        resizeKeyboard = true,
                        oneTimeKeyboard = true
                    )
                )
            }
            onText { message ->
                val model = message.content.text
                when (model) {
                    ButtonStrings.Models.Model1 -> {

                    }
                    ButtonStrings.Models.Model2 -> {

                    }
                    ButtonStrings.Models.Model3 -> {

                    }
                    ButtonStrings.Models.Model4 -> {

                    }
                    ButtonStrings.Models.Model5 -> {

                    }
                    ButtonStrings.Models.Model6 -> {

                    }
                    ButtonStrings.Models.Model7 -> {

                    }
                    ButtonStrings.Models.Model8 -> {

                    }
                }
            }
        }
    }
}

