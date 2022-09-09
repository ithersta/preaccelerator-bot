package ru.spbstu.preaccelerator.telegram.flows

import com.ithersta.tgbotapi.fsm.entities.triggers.onDataCallbackQuery
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.*
import dev.inmo.tgbotapi.types.message.MarkdownV2
import ru.spbstu.preaccelerator.domain.entities.module.Module
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.telegram.StateMachineBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.MenuState
import ru.spbstu.preaccelerator.telegram.entities.state.NewMeetingState
import ru.spbstu.preaccelerator.telegram.extensions.TeamExt.addMeeting
import ru.spbstu.preaccelerator.telegram.extensions.TrackerExt.teams
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings
import java.text.SimpleDateFormat
import java.time.ZoneOffset

fun StateMachineBuilder.addNewMeetingFlow() {
    role<Tracker> {
        state<NewMeetingState.WaitingForModuleNumber> {
            onTransition { chatId ->
                sendTextMessage(
                    chatId,
                    MessageStrings.ScheduleMeetings.InputModuleNumber,
                    parseMode = MarkdownV2
                )
            }
            onText { message ->
                val moduleNumber = message.content.text
                try {
                    if (moduleNumber.toInt() in 1..8) {
                        setState(NewMeetingState.WaitingForTeam(Module.Number(moduleNumber.toInt())))
                    } else {
                        sendTextMessage(
                            message.chat,
                            MessageStrings.ScheduleMeetings.InvalidModulNumber,
                            parseMode = MarkdownV2
                        )
                        return@onText
                    }
                }
                catch(e: Exception){
                    sendTextMessage(
                        message.chat,
                        MessageStrings.ScheduleMeetings.InvalidDataFormat + MessageStrings.ScheduleMeetings.InputModuleNumber,
                        parseMode = MarkdownV2
                    )
                    return@onText
                }
            }
        }
        state<NewMeetingState.WaitingForTeam> {
            onTransition { chatId ->
                sendTextMessage(
                    chatId,
                    MessageStrings.ScheduleMeetings.ChooseTeam,
                    replyMarkup = inlineKeyboard {
                        user.teams.chunked(2).forEach {
                            row {
                                it.forEach { dataButton(it.name, "team ${it.id.value}") }
                            }
                        }
                    }
                )
            }
            onDataCallbackQuery(Regex("team \\d+")) {
                //если пользователь введет что-то не то, выводится "Нет такой команды или она сейчас недоступна"
                val teamId = Team.Id(it.data.split(" ").last().toLong())
                setState(NewMeetingState.WaitingForUrl(state.moduleNumber, teamId))
            }
        }
        state<NewMeetingState.WaitingForUrl> {
            onTransition { chatId ->
                sendTextMessage(
                    chatId,
                    MessageStrings.ScheduleMeetings.InputUrl
                )
            }
            onText { message ->
                val url = message.content.text
                setState(NewMeetingState.WaitingForTime(state.moduleNumber, state.teamId, url))
            }
        }
        state<NewMeetingState.WaitingForTime> {
            onTransition { chatId ->
                sendTextMessage(
                    chatId,
                    MessageStrings.ScheduleMeetings.InputTime,
                    parseMode = MarkdownV2
                )
            }
            onText { message ->
                val time = try{
                    SimpleDateFormat("dd.MM.yyyy HH:mm").parse(message.content.text).toInstant()
                    .atOffset(ZoneOffset.ofHours(3))
                }
                catch(e: Exception){
                    sendTextMessage(
                        message.chat,
                        MessageStrings.ScheduleMeetings.InvalidDataFormat + MessageStrings.ScheduleMeetings.InputTime,
                        parseMode = MarkdownV2
                    )
                    return@onText
                }
                setState(NewMeetingState.CheckCorrect(state.moduleNumber, state.teamId, state.url, time))
            }
        }
        state<NewMeetingState.CheckCorrect> {
            onTransition { chatId ->
                sendTextMessage(
                    chatId,
                    MessageStrings.meetingCreationConfirmation(
                        user.teams[state.teamId.value.toInt()].name,
                        state.time,
                        state.url
                    ),
                    replyMarkup = replyKeyboard(
                        resizeKeyboard = true,
                        oneTimeKeyboard = true
                    )
                    {
                        row {
                            simpleButton(ButtonStrings.Option.Yes)
                            simpleButton(ButtonStrings.Option.No)
                        }
                    }
                )
            }
            onText { message ->
                when (message.content.text) {
                    ButtonStrings.Option.Yes -> {
                        sendTextMessage(
                            message.chat,
                            MessageStrings.ScheduleMeetings.MeetingIsCreated,
                            parseMode = MarkdownV2
                        )
                        user.teams[state.teamId.value.toInt()].addMeeting(
                            state.teamId,
                            state.moduleNumber,
                            state.time,
                            state.url
                        )
                        setState(MenuState.Tracker.Meetings)
                    }
                    ButtonStrings.Option.No -> {
                        sendTextMessage(
                            message.chat,
                            MessageStrings.ScheduleMeetings.MeetingNotCreated,
                            parseMode = MarkdownV2
                        )
                        setState(NewMeetingState.WaitingForModuleNumber)
                    }
                    else -> {
                        sendTextMessage(
                            message.chat,
                            MessageStrings.ChooseModuleAction.Err
                        )
                    }
                }
            }
        }
    }
}

