package ru.spbstu.preaccelerator.telegram.flows

import com.ithersta.tgbotapi.fsm.entities.triggers.onDataCallbackQuery
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.*
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.telegram.StateMachineBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.NewMeetingState
import ru.spbstu.preaccelerator.telegram.extensions.TrackerExt.teams
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings
import java.text.SimpleDateFormat
import java.time.ZoneOffset
import java.util.*

fun StateMachineBuilder.addNewMeetingFlow() {
    role<Tracker> {
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
                val teamId = Team.Id(it.data.split(" ").last().toLong())
                setState(NewMeetingState.WaitingForUrl(teamId))
            }
        }
        state<NewMeetingState.WaitingForUrl> {
            onTransition {
                sendTextMessage(
                    it,
                    MessageStrings.ScheduleMeetings.InputUrl
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
                time = SimpleDateFormat("dd.MM.yyyy HH:mm").parse(message.content.text).toInstant().atZone(ZoneOffset.systemDefault())
                setState(WaitingForTime(state.teamId, time))
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
