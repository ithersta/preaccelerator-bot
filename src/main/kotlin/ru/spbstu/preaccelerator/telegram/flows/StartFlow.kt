package ru.spbstu.preaccelerator.telegram.flows

import com.ithersta.tgbotapi.fsm.entities.triggers.onCommand
import com.ithersta.tgbotapi.fsm.entities.triggers.onContact
import com.ithersta.tgbotapi.fsm.entities.triggers.onDeepLink
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.flatReplyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.requestContactButton
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import dev.inmo.tgbotapi.types.message.MarkdownV2
import ru.spbstu.preaccelerator.domain.entities.PhoneNumber
import ru.spbstu.preaccelerator.domain.entities.user.Curator
import ru.spbstu.preaccelerator.domain.entities.user.EmptyUser
import ru.spbstu.preaccelerator.domain.entities.user.Member
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.telegram.StateMachineBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState
import ru.spbstu.preaccelerator.telegram.entities.state.StartFlowState
import ru.spbstu.preaccelerator.telegram.extensions.EmptyUserExt.setPhoneNumber
import ru.spbstu.preaccelerator.telegram.extensions.EmptyUserExt.useCuratorToken
import ru.spbstu.preaccelerator.telegram.extensions.MemberExt.team
import ru.spbstu.preaccelerator.telegram.extensions.TrackerExt.teams
import ru.spbstu.preaccelerator.telegram.flows.menus.curatorMenu
import ru.spbstu.preaccelerator.telegram.flows.menus.memberMenu
import ru.spbstu.preaccelerator.telegram.flows.menus.trackerMenu
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.DescriptionStrings.functionalDescription
import ru.spbstu.preaccelerator.telegram.resources.strings.HelpStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings

fun StateMachineBuilder.startFlow() {
    role<EmptyUser> {
        state<EmptyState> {
            onDeepLink { (message, token) ->
                if (!user.useCuratorToken(token)) {
                    sendTextMessage(message.chat, MessageStrings.Start.InvalidDeepLink)
                } else {
                    setState(StartFlowState.AfterAuthenticating)
                }
            }
            onCommand("start", description = HelpStrings.Start) {
                setState(StartFlowState.WaitingForContact)
            }
        }
        state<StartFlowState.WaitingForContact> {
            onTransition {
                refreshCommands()
                sendTextMessage(
                    it,
                    MessageStrings.Start.AskContact,
                    parseMode = MarkdownV2,
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
                refreshCommands()
                val text = when (val user = user) {
                    is EmptyUser -> MessageStrings.Start.NoRoleAssigned
                    is Curator -> MessageStrings.Start.WelcomeCurator
                    is Member -> MessageStrings.Start.welcomeMember(user.team)
                    is Tracker -> MessageStrings.Start.welcomeTracker(user.teams)
                }
                sendTextMessage(it, text, replyMarkup = ReplyKeyboardRemove())
                if (user !is EmptyUser) {
                    val menu = when (user) {
                        is Curator -> curatorMenu
                        is Member -> memberMenu
                        else -> trackerMenu
                    }
                    sendTextMessage(it, functionalDescription(menu), parseMode = MarkdownV2)
                }
                if ((seasonStartRepository.get() == null) && (user is Curator)) {
                    setState(StartFlowState.WaitingForStartSeason)
                }
                else {
                    setState(EmptyState)
                }
            }
        }

        state<StartFlowState.WaitingForStartSeason> {
            onTransition {
                sendTextMessage(
                    it,
                    MessageStrings.Start.StartSeason,
                    replyMarkup = inlineKeyboard {
                        row {
                            dataButton(ButtonStrings.StartNewSeason.LaunchAccelerator, "data")
                        }
                    }
                )
            }
            onDataCallbackQuery(Regex("data")){
                seasonStartRepository.set(ZonedDateTime.now(zoneId).toOffsetDateTime())
                setState(EmptyState)
            }
        }
        state<EmptyState> {
            onCommand("start", description = null) {
                setState(StartFlowState.AfterAuthenticating)
            }
        }
    }
}
