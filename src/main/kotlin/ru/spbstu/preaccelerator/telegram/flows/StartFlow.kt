package ru.spbstu.preaccelerator.telegram.flows

import com.ithersta.tgbotapi.fsm.entities.triggers.*
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.*
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import dev.inmo.tgbotapi.types.message.MarkdownV2
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.PhoneNumber
import ru.spbstu.preaccelerator.domain.entities.user.Curator
import ru.spbstu.preaccelerator.domain.entities.user.EmptyUser
import ru.spbstu.preaccelerator.domain.entities.user.Member
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.domain.repository.SeasonStartRepository
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
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun StateMachineBuilder.startFlow() {
    val seasonStartRepository: SeasonStartRepository by inject()
    val zoneId: ZoneId by inject()
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
            onText { sendTextMessage(it.chat, MessageStrings.Start.NoContactReceived) }
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
                } else {
                    setState(EmptyState)
                }
            }
        }

        state<StartFlowState.WaitingForStartSeason> {
            onTransition {
                sendTextMessage(
                    it,
                    MessageStrings.Start.NotStartedSeason,
                    replyMarkup = inlineKeyboard {
                        row {
                            dataButton(ButtonStrings.StartNewSeason.LaunchAccelerator, "data")
                        }
                    }
                )
            }
            onDataCallbackQuery(Regex("data")) {
                setState(StartFlowState.WaitingForInputDate)
            }
        }

        state<StartFlowState.WaitingForInputDate> {
            onTransition {
                sendTextMessage(
                    it,
                    MessageStrings.Start.InputDateTime
                )
            }
            onText { message ->
                val dateTime = try {
                    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").withZone(zoneId)
                    ZonedDateTime.parse(message.content.text, formatter).toOffsetDateTime()
                } catch (e: DateTimeParseException) {
                    sendTextMessage(message.chat, MessageStrings.ScheduleMeetings.InvalidDateTime)
                    return@onText
                }
                seasonStartRepository.set(dateTime)
                sendTextMessage(message.chat, MessageStrings.Start.StartSeasonSuccesfully)
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
