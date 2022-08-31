package ru.spbstu.preaccelerator.telegram.flows.commands

import com.ithersta.tgbotapi.fsm.entities.triggers.onCommand
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import ru.spbstu.preaccelerator.domain.entities.user.PreacceleratorUser
import ru.spbstu.preaccelerator.telegram.StateFilterBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.DialogState

fun StateFilterBuilder<DialogState, PreacceleratorUser>.stateCommand() {
    onCommand("state", null) {
        sendTextMessage(it.chat, state.toString())
    }
}
