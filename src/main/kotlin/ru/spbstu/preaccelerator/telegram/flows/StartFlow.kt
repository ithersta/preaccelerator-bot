package ru.spbstu.preaccelerator.telegram.flows

import com.ithersta.tgbotapi.fsm.entities.triggers.onCommand
import com.ithersta.tgbotapi.fsm.entities.triggers.onContact
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.flatReplyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.requestContactButton
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import ru.spbstu.preaccelerator.domain.entities.PhoneNumber
import ru.spbstu.preaccelerator.domain.entities.user.Curator
import ru.spbstu.preaccelerator.domain.entities.user.EmptyUser
import ru.spbstu.preaccelerator.domain.entities.user.Member
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.telegram.StateMachineBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState
import ru.spbstu.preaccelerator.telegram.entities.state.StartFlowState
import ru.spbstu.preaccelerator.telegram.extensions.EmptyUserExt.setPhoneNumber
import ru.spbstu.preaccelerator.telegram.extensions.MemberExt.team
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
                    replyMarkup = flatReplyKeyboard(resizeKeyboard = true, oneTimeKeyboard = true) {
                        requestContactButton(ButtonStrings.Start.SendContact)
                    }
                )
            }
            onContact { message ->
                val contact = message.content.contact
                require(contact.userId == message.chat.id)
                user.setPhoneNumber(PhoneNumber.of(contact.phoneNumber.filter { it.isDigit() })!!)
                setState(StartFlowState.AfterAuthenticating)
            }
        }
    }
    anyRole {
        state<StartFlowState.AfterAuthenticating> {
            onTransition {
                val text = when (val user = user) {
                    is EmptyUser -> MessageStrings.Start.NoRoleAssigned
                    is Curator -> MessageStrings.Start.WelcomeCurator
                    is Member -> MessageStrings.Start.welcomeMember(user.team)
                    is Tracker -> MessageStrings.Start.welcomeTracker(user.id)
                }
                sendTextMessage(it, text, replyMarkup = ReplyKeyboardRemove())
                setState(EmptyState)
            }
        }
    }
}
