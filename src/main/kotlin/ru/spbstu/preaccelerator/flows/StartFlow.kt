package ru.spbstu.preaccelerator.flows

import com.ithersta.tgbotapi.fsm.builders.RoleFilterBuilder
import com.ithersta.tgbotapi.fsm.entities.triggers.onCommand
import com.ithersta.tgbotapi.fsm.entities.triggers.onContact
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.flatReplyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.requestContactButton
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import ru.spbstu.preaccelerator.entities.Role
import ru.spbstu.preaccelerator.entities.state.DialogState
import ru.spbstu.preaccelerator.entities.state.EmptyState
import ru.spbstu.preaccelerator.entities.state.StartFlowState
import ru.spbstu.preaccelerator.resources.strings.ButtonStrings
import ru.spbstu.preaccelerator.resources.strings.HelpStrings
import ru.spbstu.preaccelerator.resources.strings.MessageStrings

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
