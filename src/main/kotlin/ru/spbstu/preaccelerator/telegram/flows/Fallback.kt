package ru.spbstu.preaccelerator.telegram.flows

import com.ithersta.tgbotapi.fsm.builders.StateFilterBuilder
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import ru.spbstu.preaccelerator.telegram.entities.state.DialogState
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings

fun StateFilterBuilder<DialogState, DialogState>.fallback() {
    onText {
        sendTextMessage(it.chat, MessageStrings.Fallback.NoSuchCommand)
    }
}
