package ru.spbstu.preaccelerator.telegram.flows

import com.ithersta.tgbotapi.fsm.entities.triggers.onCommand
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import ru.spbstu.preaccelerator.domain.entities.user.PreacceleratorUser
import ru.spbstu.preaccelerator.telegram.StateFilterBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.DialogState
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState
import ru.spbstu.preaccelerator.telegram.resources.strings.HelpStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings

fun StateFilterBuilder<DialogState, PreacceleratorUser>.cancelCommand() {
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
