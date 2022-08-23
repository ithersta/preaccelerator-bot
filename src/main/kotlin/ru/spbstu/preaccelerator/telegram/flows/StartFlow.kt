package ru.spbstu.preaccelerator.telegram.flows

import com.ithersta.tgbotapi.fsm.builders.RoleFilterBuilder
import com.ithersta.tgbotapi.fsm.entities.triggers.onCommand
import com.ithersta.tgbotapi.fsm.entities.triggers.onContact
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.flatReplyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.requestContactButton
import dev.inmo.tgbotapi.types.UserId
import ru.spbstu.preaccelerator.domain.entities.Role
import ru.spbstu.preaccelerator.telegram.entities.state.DialogState
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState
import ru.spbstu.preaccelerator.telegram.entities.state.StartFlowState
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.HelpStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings

fun RoleFilterBuilder<Role, DialogState, UserId>.startFlow() {
    state<EmptyState> {
        onCommand("start", description = HelpStrings.Start) {
            setState(StartFlowState.WaitingForContact)
        }
    }
    state<StartFlowState.WaitingForContact> {
        onTransition {
            sendTextMessage(
                it,
                MessageStrings.Start.AskContact,
                replyMarkup = flatReplyKeyboard {
                    requestContactButton(ButtonStrings.Start.SendContact)
                }
            )
        }
        onContact {
            setState(StartFlowState.After)
        }
    }
    state<StartFlowState.After> {
        onTransition {
            sendTextMessage(it, MessageStrings.Start.NoRoleAssigned)
            setState(EmptyState)
        }
    }
}
