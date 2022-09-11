package ru.spbstu.preaccelerator.telegram.notifications


import com.ithersta.tgbotapi.fsm.entities.triggers.onDataCallbackQuery
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.bot.exceptions.CommonBotException
import dev.inmo.tgbotapi.extensions.api.edit.reply_markup.editMessageReplyMarkup
import dev.inmo.tgbotapi.extensions.api.edit.text.editMessageText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.messageCallbackQueryOrThrow
import dev.inmo.tgbotapi.extensions.utils.types.buttons.dataButton
import dev.inmo.tgbotapi.extensions.utils.types.buttons.inlineKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.row
import dev.inmo.tgbotapi.types.UserId
import dev.inmo.tgbotapi.types.buttons.InlineKeyboardMarkup
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import dev.inmo.tgbotapi.types.toChatId
import dev.inmo.tgbotapi.types.userField
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.user.*
import ru.spbstu.preaccelerator.domain.repository.MemberRepository
import ru.spbstu.preaccelerator.domain.repository.TeamRepository
import ru.spbstu.preaccelerator.domain.repository.TrackerRepository
import ru.spbstu.preaccelerator.domain.repository.UserPhoneNumberRepository
import ru.spbstu.preaccelerator.telegram.RoleFilterBuilder
import ru.spbstu.preaccelerator.telegram.StateFilterBuilder
import ru.spbstu.preaccelerator.telegram.StateMachineBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.DialogState
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState
import ru.spbstu.preaccelerator.telegram.entities.state.SendInfoState
import ru.spbstu.preaccelerator.telegram.entities.state.TypeMassMess
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.NotificationStrings
import java.lang.Exception
import java.util.StringJoiner

fun StateMachineBuilder.sendingInfo() {
    val teamRepository: TeamRepository by inject()
    val userPhoneNumberRepository: UserPhoneNumberRepository by inject()
    val trackerRepository: TrackerRepository by inject()
    anyRole {
        state<SendInfoState> {
            onTransition { messenger ->
                if (!state.startSending && (state.typeMassMess == TypeMassMess.TeamsFromCuratorAndTacker)) {
                    if (state.messageIdentifier == null) {
                        val listNamesTeams = when (user) {
                            is Curator -> teamRepository.getAll().map { it.id.value to it.name }
                            is Tracker -> teamRepository.get(
                                trackerRepository.get(
                                    userPhoneNumberRepository.get(
                                        messenger.chatId.toChatId()
                                    )!!
                                )!!.id
                            )
                                .map { it.id.value to it.name }

                            else -> {
                                require(false) //user is not Curator or Tracker
                                listOf(0L to "")
                            }
                        }
                        val newState = state.copy(
                            listNamesTeams = listNamesTeams,
                            mutableSetTeamId = mutableSetOf(),
                            messageIdentifier = sendTextMessage(
                                chatId = messenger,
                                text = NotificationStrings.MassSendInfo.listOfTeams,
                                replyMarkup = inlineTeams(
                                    listNamesTeams,
                                    mutableSetOf()
                                )
                            ).messageId
                        )
                        setState(newState)
                    } else {
                        try {
                            editMessageReplyMarkup(
                                chatId = messenger,
                                messageId = state.messageIdentifier!!,
                                replyMarkup = inlineTeams(
                                    state.listNamesTeams!!,
                                    state.mutableSetTeamId!!
                                )
                            )
                        } catch (_: CommonBotException) {
                        }
                    }
                } else if (state.infoMess.isNotEmpty()) {
                    if (state.messageIdentifier == null) {
                        val newState = state.copy(
                            messageIdentifier = sendTextMessage(
                                chatId = messenger,
                                text = NotificationStrings.MassSendInfo.requestConfirm(state.infoMess),
                                replyMarkup = inlineKeyboard {
                                    row {
                                        dataButton(NotificationStrings.MassSendInfo.send, "send info")
                                    }
                                    row {
                                        dataButton(
                                            NotificationStrings.MassSendInfo.inputOtherMessage,
                                            "input other message"
                                        )
                                    }
                                }
                            ).messageId
                        )
                        setState(newState)
                    }
                } else {
                    sendTextMessage(
                        chatId = messenger,
                        text = NotificationStrings.MassSendInfo.inputOneMessage,
                        replyMarkup = ReplyKeyboardRemove()
                    ).messageId
                }
            }
            onText { message ->
                if (state.startSending) {
                    val newState = state.copy(
                        infoMess = message.content.text
                    )
                    setState(newState)
                }
            }
            onDataCallbackQuery(Regex("input other message")) { message ->
                editMessageReplyMarkup(
                    chat = message.from,
                    messageId = state.messageIdentifier!!,
                    replyMarkup = null
                )
                val newState = state.copy(
                    messageIdentifier = null,
                    infoMess = ""
                )
                setState(newState)
            }
            onDataCallbackQuery(Regex("add:\\d+")) { message ->
                state.mutableSetTeamId!!.add(message.data.split(":")[1].toLong())
                setState(state)
            }
            onDataCallbackQuery(Regex("del:\\d+")) { message ->
                state.mutableSetTeamId!!.remove(message.data.split(":")[1].toLong())
                setState(state)
            }
            onDataCallbackQuery(Regex("send such teams")) { message ->
                editMessageText(
                    chat = message.from,
                    messageId = message.messageCallbackQueryOrThrow().message.messageId,
                    text = NotificationStrings.MassSendInfo.listTeams(
                        state.mutableSetTeamId!!,
                        state.listNamesTeams!!.toMap()
                    ),
                    replyMarkup = null
                )
                val newState = state.copy(
                    messageIdentifier = null,
                    startSending = true
                )
                setState(newState)
            }
            onDataCallbackQuery(Regex("send info")) { message ->
                editMessageReplyMarkup(
                    chat = message.from,
                    messageId = message.messageCallbackQueryOrThrow().message.messageId,
                    replyMarkup = null
                )
                sendTextMessage(message.from, NotificationStrings.MassSendInfo.startSendInfo)
                val massSendLimiter: MassSendLimiter by inject()
                val messageMass = when (user) {
                    is Curator -> NotificationStrings.MassSendInfo.notificationCurator(state.infoMess)
                    is Tracker -> NotificationStrings.MassSendInfo.notificationTracker(state.infoMess)
                    else -> {
                        require(true) //user is not Curator or Tracker
                        ""
                    }
                }
                when (state.typeMassMess) {
                    TypeMassMess.AllFromCurator -> {
                        val trackerRepository: TrackerRepository by inject()
                        trackerRepository.getAll().forEach {
                            massSendLimiter.wait()
                            sendTextMessage(
                                chatId = UserId(it.id.value),
                                text = messageMass
                            )
                        }
                        val memberRepository: MemberRepository by inject()
                        memberRepository.getAll().forEach {
                            massSendLimiter.wait()
                            sendTextMessage(
                                chatId = UserId(it.id.value),
                                text = messageMass
                            )
                        }
                    }

                    TypeMassMess.TrackersFromCurator -> {
                        val trackerRepository: TrackerRepository by inject()
                        trackerRepository.getAll().forEach { tracker ->
                            massSendLimiter.wait()
                            sendTextMessage(
                                chatId = UserId(tracker.id.value),
                                text = messageMass
                            )
                        }
                    }

                    TypeMassMess.TeamsFromCuratorAndTacker -> {
                        val memberRepository: MemberRepository by inject()
                        state.mutableSetTeamId!!.forEach { teamId ->
                            memberRepository.get(Team.Id(teamId)).forEach { member ->
                                sendTextMessage(
                                    chatId = UserId(member.id.value),
                                    text = messageMass
                                )
                            }
                        }
                    }

                    TypeMassMess.AllFromTracker -> {
                        val memberRepository: MemberRepository by inject()
                        teamRepository.get(Tracker.Id(message.user.id.chatId)).forEach { team ->
                            memberRepository.get(team.id).forEach { member ->
                                massSendLimiter.wait()
                                sendTextMessage(
                                    chatId = UserId(member.id.value),
                                    text = messageMass
                                )
                            }
                        }
                    }
                }
                sendTextMessage(message.from, NotificationStrings.MassSendInfo.finishSendInfo)
                setState(EmptyState)
            }
        }
    }

}

private fun inlineTeams(teams: List<Pair<Long, String>>, mutableSetTeamId: MutableSet<Long>): InlineKeyboardMarkup {
    return inlineKeyboard {
        teams.chunked(2).forEach { chunk ->
            row {
                chunk.forEach { (id, name) ->
                    if (mutableSetTeamId.contains(id)) {
                        dataButton("-${name}", "del:${id}")
                    } else {
                        dataButton("+${name}", "add:${id}")
                    }
                }
            }
        }
        row {
            dataButton(NotificationStrings.MassSendInfo.sendSuchTeams, "send such teams")
        }
    }
}