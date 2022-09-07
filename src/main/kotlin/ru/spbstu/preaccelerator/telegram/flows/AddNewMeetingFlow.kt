package ru.spbstu.preaccelerator.telegram.flows

import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.replyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.row
import dev.inmo.tgbotapi.extensions.utils.types.buttons.simpleButton
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.telegram.StateMachineBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.CheckCorrect
import ru.spbstu.preaccelerator.telegram.entities.state.NewMeeting
import ru.spbstu.preaccelerator.telegram.entities.state.TimeMeeting
import ru.spbstu.preaccelerator.telegram.entities.state.UrlMeeting
import ru.spbstu.preaccelerator.telegram.extensions.TrackerExt.teams
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings
import java.text.SimpleDateFormat
import java.time.ZoneOffset
import java.util.*


private var teamName = ""
private var url = ""
private var time = Date(System.currentTimeMillis()).toInstant().atOffset(ZoneOffset.UTC)

fun StateMachineBuilder.addNewMeetingFlow() {
    role<Tracker> {
        state<NewMeeting> {
            onTransition {
                sendTextMessage(
                    it,
                    MessageStrings.ScheduleMeetings.ChooseTeam,
                    replyMarkup = replyKeyboard(
                        resizeKeyboard = true,
                        oneTimeKeyboard = true
                    )
                    {
                        user.teams.chunked(2).forEach {
                            row {
                                it.forEach { simpleButton(it.name) }
                            }
                        }
                    }
                )
            }
            onText { message ->
                teamName = message.content.text
                setState(UrlMeeting)
            }
        }
        state<UrlMeeting> {
            onTransition {
                sendTextMessage(
                    it,
                    MessageStrings.ScheduleMeetings.InputURL
                )
            }
            onText { message ->
                url = message.content.text
                setState(TimeMeeting)
            }
        }
        state<TimeMeeting> {
            onTransition {
                sendTextMessage(
                    it,
                    MessageStrings.ScheduleMeetings.InputTime
                )
            }
            onText { message ->
                time = SimpleDateFormat("dd.MM.yyyy HH:mm").parse(message.content.text).toInstant().atOffset(ZoneOffset.ofHours(3))
                setState(CheckCorrect)
            }
        }
        state<CheckCorrect> {
            onTransition {
                sendTextMessage(
                    it,
                    MessageStrings.meetingCreationConfirmation(teamName, time, url),
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
        }
    }
}