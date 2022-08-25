package ru.spbstu.preaccelerator.telegram.flows

import com.ithersta.tgbotapi.fsm.entities.triggers.onCommand
import com.ithersta.tgbotapi.fsm.entities.triggers.onContact
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.flatReplyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.requestContactButton
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import ru.spbstu.preaccelerator.domain.entities.user.Curator
import ru.spbstu.preaccelerator.domain.entities.user.EmptyUser
import ru.spbstu.preaccelerator.domain.entities.user.Member
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.telegram.StateMachineBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState
import ru.spbstu.preaccelerator.telegram.entities.state.StartFlowState
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.HelpStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings

fun StateMachineBuilder.startFlow() {
    role<EmptyUser> {
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
                    replyMarkup = flatReplyKeyboard(resizeKeyboard = true) {
                        requestContactButton(ButtonStrings.Start.SendContact)
                    }
                )
            }
            onContact {
                val contact = it.content.contact
                require(contact.userId == it.chat.id)
                setState(StartFlowState.AfterRegistering)
            }
        }
        state<StartFlowState.AfterRegistering> {
            onTransition {
                sendTextMessage(it, MessageStrings.Start.NoRoleAssigned, replyMarkup = ReplyKeyboardRemove())
                setState(EmptyState)
            }
        }
    }
    role<Curator> {
        state<StartFlowState.AfterRegistering> {
            onTransition { }
        }
    }
    role<Tracker> {
        state<StartFlowState.AfterRegistering> {
            onTransition { }
        }
    }
    role<Member> {
        state<StartFlowState.AfterRegistering> {
            onTransition { }
        }
    }
}
