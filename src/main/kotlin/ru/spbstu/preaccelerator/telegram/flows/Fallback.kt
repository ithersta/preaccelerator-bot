package ru.spbstu.preaccelerator.telegram.flows

import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import ru.spbstu.preaccelerator.domain.entities.user.PreacceleratorUser
import ru.spbstu.preaccelerator.telegram.StateFilterBuilder
import ru.spbstu.preaccelerator.telegram.StateMachineBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.DialogState
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings

fun StateMachineBuilder.fallback() {
    anyRole {
        anyState {
            onText {
                sendTextMessage(it.chat, MessageStrings.Fallback.NoSuchCommand)
            }
        }
    }
}
