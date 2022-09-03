package ru.spbstu.preaccelerator.telegram.flows

import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import ru.spbstu.preaccelerator.telegram.StateMachineBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState
import ru.spbstu.preaccelerator.telegram.entities.state.NotImplementedState
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings

fun StateMachineBuilder.fallback() {
    anyRole {
        state<NotImplementedState> {
            onTransition {
                sendTextMessage(it, MessageStrings.Fallback.NotImplemented)
                setState(EmptyState)
            }
        }
        anyState {
            onText {
                sendTextMessage(it.chat, MessageStrings.Fallback.NoSuchCommand)
                setState(state)
            }
        }
    }
}
