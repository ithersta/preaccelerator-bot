package ru.spbstu.preaccelerator.flows

import com.ithersta.tgbotapi.fsm.builders.StateFilterBuilder
import com.ithersta.tgbotapi.fsm.entities.triggers.onCommand
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import ru.spbstu.preaccelerator.entities.state.DialogState
import ru.spbstu.preaccelerator.entities.state.EmptyState
import ru.spbstu.preaccelerator.resources.strings.HelpStrings
import ru.spbstu.preaccelerator.resources.strings.MessageStrings

fun StateFilterBuilder<DialogState, DialogState>.cancelCommand() {
    onCommand("cancel", HelpStrings.Cancel) {
        setState(EmptyState)
        sendTextMessage(
            it.chat,
            text = if (state == EmptyState) {
                MessageStrings.Cancel.NothingToCancel
            } else {
                MessageStrings.Cancel.Success
            },
            replyMarkup = ReplyKeyboardRemove()
        )
    }
}
