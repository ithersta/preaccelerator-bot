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
import ru.spbstu.preaccelerator.domain.usecases.GetListTeamsUseCase
import ru.spbstu.preaccelerator.telegram.RoleFilterBuilder
import ru.spbstu.preaccelerator.telegram.StateFilterBuilder
import ru.spbstu.preaccelerator.telegram.StateMachineBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.*
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.NotificationStrings
import java.lang.Exception
import java.util.StringJoiner

fun StateMachineBuilder.sendingInfo() {
    val userPhoneNumberRepository: UserPhoneNumberRepository by inject()
    val trackerRepository: TrackerRepository by inject()
    val memberRepository: MemberRepository by inject()
    val getListTeamsUseCase: GetListTeamsUseCase by inject()

    anyRole {
        state<SendInfoState> {
            onTransition { messeger ->
                if (state.typeMassMess == TypeMassMess.TeamsFromCuratorAndTacker) {
                    if (state.messageIdentifier == null) {
                        val newState = state.copy(
                            setTeamId = setOf(),
                            messageIdentifier = sendTextMessage(
                                chatId = messeger,
                                text = MessageStrings.MassSendInfo.ListOfTeams,
                                replyMarkup = inlineTeams(
                                    getListTeamsUseCase(user),
                                    mutableSetOf()
                                )
                            ).messageId
                        )
                        setState(newState)
                    } else {
                        try {
                            editMessageReplyMarkup(
                                chatId = messeger,
                                messageId = state.messageIdentifier!!,
                                replyMarkup = inlineTeams(
                                    getListTeamsUseCase(user),
                                    state.setTeamId!!
                                )
                            )
                        } catch (_: CommonBotException) {
                        }
                    }
                } else {
                    val listUsers = when (state.typeMassMess) {
                        TypeMassMess.AllFromCurator -> {
                            val list = trackerRepository.getAll().map { userPhoneNumberRepository.get(it.phoneNumber) }
                                .toMutableList()
                            list.addAll(memberRepository.getAll().map { userPhoneNumberRepository.get(it.phoneNumber) })
                            list.filterNotNull()
                        }

                        TypeMassMess.TrackersFromCurator ->
                            trackerRepository.getAll().mapNotNull { userPhoneNumberRepository.get(it.phoneNumber) }

                        TypeMassMess.AllFromTracker -> {
                            val mutableList = mutableListOf<UserId>()
                            getListTeamsUseCase(user)
                                .forEach { team ->
                                    memberRepository.get(team.id).forEach { member ->
                                        val userId = userPhoneNumberRepository.get(member.phoneNumber)
                                        if (userId != null) {
                                            mutableList.add(userId)
                                        }
                                    }
                                }
                            mutableList.toList()
                        }

                        else -> listOf()
                    }
                    val newState = InputMess(
                        listUserId = listUsers.map { it.chatId },
                        massMess = ""
                    )
                    setState(newState)
                }
            }
            onDataCallbackQuery(Regex("add:\\d+")) { message ->
                val newSetTeamId = state.setTeamId!!.toMutableSet()
                newSetTeamId.add(Team.Id(message.data.split(":")[1].toLong()))
                val newState = state.copy(
                    setTeamId = newSetTeamId
                )
                setState(newState)
            }
            onDataCallbackQuery(Regex("del:\\d+")) { message ->
                val newSetTeamId = state.setTeamId!!.toMutableSet()
                newSetTeamId.remove(Team.Id(message.data.split(":")[1].toLong()))
                val newState = state.copy(
                    setTeamId = newSetTeamId
                )
                setState(newState)
            }
            onDataCallbackQuery(Regex("send such teams")) { message ->
                editMessageText(
                    chat = message.from,
                    messageId = message.messageCallbackQueryOrThrow().message.messageId,
                    text = MessageStrings.MassSendInfo.listTeams(
                        state.setTeamId!!,
                        getListTeamsUseCase(user).associate { it.id to it.name }
                    ),
                    replyMarkup = null
                )
                val listUsers = mutableListOf<UserId>()
                state.setTeamId!!.forEach { teamId ->
                    memberRepository.get(teamId).forEach { member ->
                        val userId = userPhoneNumberRepository.get(member.phoneNumber)
                        if (userId != null) {
                            listUsers.add(userId)
                        }
                    }
                }
                val newState = InputMess(
                    listUserId = listUsers.map { it.chatId },
                    massMess = ""
                )
                setState(newState)
            }
        }
        state<InputMess> {
            onTransition { messager ->
                if (state.massMess.isEmpty()) {
                    sendTextMessage(
                        chatId = messager,
                        text = MessageStrings.MassSendInfo.InputOneMessage
                    )
                } else {
                    sendTextMessage(
                        chatId = messager,
                        text = MessageStrings.MassSendInfo.requestConfirm(state.massMess),
                        replyMarkup = inlineKeyboard {
                            row {
                                dataButton(ButtonStrings.MassSendInfo.Send, "send info")
                            }
                            row {
                                dataButton(ButtonStrings.MassSendInfo.InputOtherMessage, "input other message")
                            }
                        }
                    )
                }
            }
            onText { message ->
                val newState = state.copy(
                    massMess = message.content.text
                )
                setState(newState)
            }
            onDataCallbackQuery(Regex("input other message")) { message ->
                editMessageReplyMarkup(
                    chat = message.from,
                    messageId = message.messageCallbackQueryOrThrow().message.messageId,
                    replyMarkup = null
                )
                val newState = state.copy(
                    massMess = ""
                )
                setState(newState)
            }
            onDataCallbackQuery(Regex("send info")) { message ->
                sendTextMessage(
                    chat = message.from,
                    text = MessageStrings.MassSendInfo.StartSendInfo
                )
                editMessageReplyMarkup(
                    chat = message.from,
                    messageId = message.messageCallbackQueryOrThrow().message.messageId,
                    replyMarkup = null
                )
                val massMessage = when (user) {
                    is Curator -> NotificationStrings.MassSendInfo.notificationCurator(state.massMess)
                    is Tracker -> NotificationStrings.MassSendInfo.notificationTracker(state.massMess)
                    else -> {
                        error("user is not curator or tracker")
                    }
                }
                val massSendLimiter: MassSendLimiter by inject()
                state.listUserId.forEach { chatId ->
                    massSendLimiter.wait()
                    sendTextMessage(
                        chatId = UserId(chatId),
                        text = massMessage
                    )
                }
                sendTextMessage(
                    chat = message.from,
                    text = MessageStrings.MassSendInfo.FinishSendInfo
                )
                setState(EmptyState)
            }
        }
    }


}

private fun inlineTeams(teams: List<Team>, setTeamId: Set<Team.Id>): InlineKeyboardMarkup {
    return inlineKeyboard {
        teams.chunked(2).forEach { chunk ->
            row {
                chunk.forEach { team ->
                    if (setTeamId.contains(team.id)) {
                        dataButton("-${team.name}", "del:${team.id.value}")
                    } else {
                        dataButton("+${team.name}", "add:${team.id.value}")
                    }
                }
            }
        }
        row {
            dataButton(ButtonStrings.MassSendInfo.SendSuchTeams, "send such teams")
        }
    }
}