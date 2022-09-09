package ru.spbstu.preaccelerator.telegram.flows

import com.ithersta.tgbotapi.fsm.entities.triggers.onDataCallbackQuery
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.*
import ru.spbstu.preaccelerator.domain.entities.Meeting
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module.Number
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.telegram.StateMachineBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.*
import ru.spbstu.preaccelerator.telegram.extensions.TeamExt.availableModules
import ru.spbstu.preaccelerator.telegram.extensions.TeamExt.protocol
import ru.spbstu.preaccelerator.telegram.extensions.TrackerExt.teams
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.ChooseModule
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.ChooseTeam
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.InputGoogleDiskUrl
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.confirmationProtocol
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

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
        state<ChooseTeam> {
//              команды
            onTransition { chatId ->
                sendTextMessage(chatId,
                    ChooseTeam,
                    replyMarkup = inlineKeyboard {
                        user.teams.chunked(2).forEach {
                            row {
                                it.forEach { dataButton(it.name, "teamId ${it.id}") }
                            }
                        }
                    })
            }
            onDataCallbackQuery(Regex("teamId \\d+")) {
                val teamId = Team.Id(it.data.split(" ").last().toLong())
                setState(ChooseModule(teamId))
            }
        }
//        модули
        state<ChooseModule> {
            onTransition { chatId ->
                sendTextMessage(chatId,
                    ChooseModule,
                    replyMarkup = inlineKeyboard {
                        user.teams[state.teamId.value.toInt()].availableModules.chunked(2).forEach {
                            row {
                                it.forEach {
                                    dataButton(it.name, "moduleId ${it.number}")
                                }
                            }
                        }
                    })
            }
            onDataCallbackQuery(Regex("moduleId \\d+")) {
                val moduleNumber = Number(it.data.split(' ').last().toInt())
                val team = user.teams[state.teamId.value.toInt()]
                if (team.protocol == null) {
                    setState(SendDiskUrl(state.teamId, moduleNumber))
                } else {
                    setState(fillInTheProtocolWithData(state.teamId, moduleNumber, team.protocol!!.url))
                }
            }

        }
//        ссылка
        state<SendDiskUrl> {
            onTransition { chatId ->
                sendTextMessage(chatId, InputGoogleDiskUrl)
            }
            onText { message ->
                val googleDiskLink = message.content.text
                setState(fillInTheProtocolWithData(state.teamId, state.moduleNumber, googleDiskLink))
            }
        }
//          отправка
        state<fillInTheProtocolWithData> {
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
//                sendTextMessage(chatId, SentCurator)
//                val team = user.teams[giveIdByName(user.teams, teamName) - 1]
//                val time1: OffsetDateTime = stringToOffsetDateTime(time)
                придумать как нормально обратно OffsetDateTime взять из string -а
//                val meeting = team.meetings[giveIdByTime(team.meetings, time1) - 1]
//                meeting.addProtocol(meeting.id, UrlGoogleDisk)
            }
        }
    }
}