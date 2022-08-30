package ru.spbstu.preaccelerator.telegram.flows

import com.ithersta.tgbotapi.fsm.entities.triggers.onCommand
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.extensions.raw.text
import dev.inmo.tgbotapi.extensions.utils.types.buttons.ReplyKeyboardMarkup
import dev.inmo.tgbotapi.types.buttons.SimpleKeyboardButton
import dev.inmo.tgbotapi.utils.RiskFeature
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.telegram.StateMachineBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.StartFlowState
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings

private fun arrayOfSimpleKeyboardButtonsForArray(arr: Array<String>): Array<SimpleKeyboardButton> {
    return arr.map { SimpleKeyboardButton(it) }.toTypedArray()
}

fun StateMachineBuilder.button() {
    role<Tracker> {
        state<StartFlowState.AfterAuthenticating> {
            onCommand("modul", description = ButtonStrings.ChooseStep) {
                setState(StartFlowState.AfterAuthenticating)
            }
        }
        state<StartFlowState.AfterAuthenticating> {
            onTransition {
                refreshCommands()
                sendTextMessage(
                    it, ButtonStrings.ChooseStep,
                    replyMarkup = ReplyKeyboardMarkup(
                        buttons = arrayOfSimpleKeyboardButtonsForArray(ButtonStrings.listOfModels.toTypedArray()),
                        resizeKeyboard = true,
                        oneTimeKeyboard = true
                    )
                ).text
            }
        }
    }
}

