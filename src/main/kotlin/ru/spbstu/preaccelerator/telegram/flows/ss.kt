package ru.spbstu.preaccelerator.telegram.flows

import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.replyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.row
import dev.inmo.tgbotapi.extensions.utils.types.buttons.simpleButton
import dev.inmo.tgbotapi.types.message.MarkdownV2
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.telegram.StateMachineBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.*
import ru.spbstu.preaccelerator.telegram.extensions.TeamExt.meeting
import ru.spbstu.preaccelerator.telegram.extensions.TrackerExt.teams
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings.Tracker.Meetings.chooseMeeting
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings.Tracker.Meetings.chooseTeam
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings.Tracker.Meetings.confirmationProtocol
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings.Tracker.Meetings.inputGoogleDiskUrl
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings.Tracker.Meetings.sentCurator
import java.text.SimpleDateFormat
import java.time.ZoneOffset
import java.util.*


private var teamName = String()
private var time = String()
private var URLGoogleDisk = String()

private fun giveIdByName(teams: List<Team>, name: String): Int {
    return teams.find { it.name == name }!!.id.value.toInt()
}

fun StateMachineBuilder.ss() {
    role<Tracker> {
        state<SendProtocolState> {
            onTransition { chatId ->
                sendTextMessage(chatId,
                    chooseTeam,
                    parseMode = MarkdownV2,
                    replyMarkup = replyKeyboard(resizeKeyboard = true, oneTimeKeyboard = true) {
                        user.teams
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
                        user.teams[giveIdByName(user.teams, teamName) - 1].meeting.chunked(2).forEach {
                            row {
                                it.forEach {
                                    // todo :

                                    simpleButton(it.timestamp.toString())
                                }
                            }
                        }
                    }
                )

            }
            onText { messsage ->
                time = messsage.content.text
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
                sendTextMessage(chatId, sentCurator)
            }
        }
    }
}