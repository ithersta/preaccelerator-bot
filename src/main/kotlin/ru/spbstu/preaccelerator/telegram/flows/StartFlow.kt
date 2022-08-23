package ru.spbstu.preaccelerator.telegram.flows

import com.ithersta.tgbotapi.fsm.builders.RoleFilterBuilder
import com.ithersta.tgbotapi.fsm.entities.triggers.onCommand
import com.ithersta.tgbotapi.fsm.entities.triggers.onContact
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.flatReplyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.requestContactButton
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import ru.spbstu.preaccelerator.domain.entities.Role
import ru.spbstu.preaccelerator.telegram.entities.state.DialogState
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState
import ru.spbstu.preaccelerator.telegram.entities.state.StartFlowState
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.HelpStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings

fun RoleFilterBuilder<Role, DialogState>.startFlow() {
    state<EmptyState> {
        onCommand("start", description = HelpStrings.Start) {
            setState(StartFlowState.WaitingForContact)
            sendTextMessage(
                it.chat,
                MessageStrings.Start.AskContact,
                replyMarkup = flatReplyKeyboard {
                    requestContactButton(ButtonStrings.Start.SendContact)
                }
            )
        }
    }
    state<StartFlowState.WaitingForContact> {
        onContact {
            setState(EmptyState)
            sendTextMessage(
                it.chat,
                "got ${it.content.contact.phoneNumber}",
                replyMarkup = ReplyKeyboardRemove()
            )
        }
    }
}
