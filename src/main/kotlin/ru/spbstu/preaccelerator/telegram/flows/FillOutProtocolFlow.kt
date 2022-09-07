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
import ru.spbstu.preaccelerator.telegram.StateMachineBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.*
import ru.spbstu.preaccelerator.telegram.extensions.MeetingExt.addProtocol
import ru.spbstu.preaccelerator.telegram.extensions.TeamExt.meetings
import ru.spbstu.preaccelerator.telegram.extensions.TrackerExt.teams
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.ChooseMeeting
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.ChooseTeam
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.InputGoogleDiskUrl
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.SentCurator
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.confirmationProtocol
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private var teamName = String()
private var time = String()
private var UrlGoogleDisk = String()
private const val form = "dd\\.MM\\.yyyy HH:mm"
private val formatter = DateTimeFormatter.ofPattern(form)

private fun giveIdByName(teams: List<Team>, name: String): Int {
    return teams.find { it.name == name }!!.id.value.toInt()
}

private fun giveIdByTime(meetings: List<Meeting>, time: OffsetDateTime): Int {
    return meetings.find { it.timestamp == time }!!.id.value.toInt()
}

private fun stringToOffsetDateTime(text: String): OffsetDateTime {
    return SimpleDateFormat(form)
        .parse(text).toInstant().atOffset(ZoneOffset.ofHours(3))
}


fun StateMachineBuilder.fillOutProtocolFlow() {
    role<Tracker> {
        state<SendProtocolState> {
            onTransition { chatId ->
                sendTextMessage(chatId,
                    ChooseTeam,
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
                    ChooseMeeting,
                    replyMarkup = replyKeyboard(resizeKeyboard = true, oneTimeKeyboard = true) {
                        user.teams[giveIdByName(user.teams, teamName) - 1].meetings.chunked(2).forEach {
                            row {
                                it.forEach {
                                    simpleButton(it.timestamp.format(formatter))
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
                sendTextMessage(chatId, InputGoogleDiskUrl, parseMode = MarkdownV2)
            }
            onText { message ->
                UrlGoogleDisk = message.content.text
                setState(CheckCorrectInput)
            }
        }
        state<CheckCorrectInput> {
            onTransition { chatId ->
                sendTextMessage(chatId,
                    confirmationProtocol(teamName, time),
                    replyMarkup = replyKeyboard(resizeKeyboard = true, oneTimeKeyboard = true) {
                        row {
                            simpleButton(ButtonStrings.Option.Yes)
                            simpleButton(ButtonStrings.Option.No)
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
                val team = user.teams[giveIdByName(user.teams, teamName) - 1]
                val time1: OffsetDateTime = stringToOffsetDateTime(time)
                // придумать как нормально обратно OffsetDateTime взять из string-а
                val meeting = team.meetings[giveIdByTime(team.meetings, time1) - 1]
                meeting.addProtocol(meeting.id, UrlGoogleDisk)
            }
        }
    }
}