package ru.spbstu.preaccelerator.telegram.flows

import com.ithersta.tgbotapi.fsm.builders.StateFilterBuilder
import com.ithersta.tgbotapi.fsm.entities.triggers.onCommand
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.types.UserId
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import ru.spbstu.preaccelerator.telegram.entities.state.DialogState

fun StateFilterBuilder<DialogState, DialogState, UserId>.stateCommand() {
    onCommand("state", null) {
        sendTextMessage(it.chat, state.toString())
    }
}
