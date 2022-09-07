package ru.spbstu.preaccelerator.telegram.flows

import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.replyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.row
import dev.inmo.tgbotapi.extensions.utils.types.buttons.simpleButton
import dev.inmo.tgbotapi.types.message.MarkdownV2
import ru.spbstu.preaccelerator.domain.entities.Meeting
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.domain.repository.ProtocolRepository
import ru.spbstu.preaccelerator.telegram.StateMachineBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.*
import ru.spbstu.preaccelerator.telegram.extensions.MeetingExt.addProtocol
import ru.spbstu.preaccelerator.telegram.extensions.MeetingExt.protocol
import ru.spbstu.preaccelerator.telegram.extensions.TeamExt.addHomework
import ru.spbstu.preaccelerator.telegram.extensions.TeamExt.meetings
import ru.spbstu.preaccelerator.telegram.extensions.TrackerExt.teams
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.ChooseMeeting
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.ChooseTeam
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.confirmationProtocol
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.InputGoogleDiskUrl
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.SentCurator
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.ZoneOffset

private var teamName = String()
private var time = String()
private var URLGoogleDisk = String()

private fun giveIdByName(teams: List<Team>, name: String): Int {
    return teams.find { it.name == name }!!.id.value.toInt()
}

private fun giveIdByTime(meetings: List<Meeting>, time: OffsetDateTime): Int {
    return meetings.find { it.timestamp == time }!!.id.value.toInt()
}

private fun stringToOffsetDateTime(text: String): OffsetDateTime {
    return SimpleDateFormat("yyyy-MM-dd  HH:mm:ss ")
        .parse(text).toInstant().atOffset(ZoneOffset.ofHours(3))
}


fun StateMachineBuilder.fillOutProtocolFlow() {
    role<Tracker> {
        state<SendProtocolState> {
            onTransition { chatId ->
                sendTextMessage(chatId,
                    chooseTeam,
                    parseMode = MarkdownV2,
                    replyMarkup = replyKeyboard(resizeKeyboard = true, oneTimeKeyboard = true) {
                        user.teams.chunked(2).forEach {
                            row {
                                it.forEach { simpleButton(it.name) }
                            }
                        }
                    })
            }
            onText { message ->
                teamName = message.content.text
                setState(ChooseMeetingState)
            }
        }
        state<ChooseMeetingState> {
            onTransition { chatId ->
                sendTextMessage(chatId,
                    chooseMeeting,
                    parseMode = MarkdownV2,
                    replyMarkup = replyKeyboard(resizeKeyboard = true, oneTimeKeyboard = true) {
                        user.teams[giveIdByName(user.teams, teamName) - 1].meetings.chunked(2).forEach {
                            row {
                                it.forEach {
                                    simpleButton(it.timestamp.)
                                }
                            }
                        }
                    })
            }
            onText { message ->
                time = message.content.text
                setState(UrlGoogleDocState)
            }
        }
        state<UrlGoogleDocState> {
            onTransition { chatId ->
                sendTextMessage(chatId, inputGoogleDiskUrl, parseMode = MarkdownV2)
            }
            onText { message ->
                URLGoogleDisk = message.content.text
                setState(CheckCorrectInput)
            }
        }
        state<CheckCorrectInput> {
            onTransition { chatId ->
                sendTextMessage(chatId,
                    confirmationProtocol(teamName, time),
                    parseMode = MarkdownV2,
                    replyMarkup = replyKeyboard(resizeKeyboard = true, oneTimeKeyboard = true) {
                        row {
                            simpleButton("Да")
                            simpleButton("Нет")
                        }
                    })
            }
            onText { message ->
                val answer = message.content.text
                setState(FinalSendProtocolState)
            }
        }

        state<FinalSendProtocolState> {
            onTransition { chatId ->
                sendTextMessage(chatId, SentCurator)
                val team = user.teams[giveIdByName(user.teams, teamName)]
                val time2: OffsetDateTime = stringToOffsetDateTime(time)
                val meeting = team.meetings[giveIdByTime(team.meetings, time2)]
                meeting.addProtocol(meeting.id, URLGoogleDisk)
            }
        }
    }
}