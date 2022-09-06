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
import ru.spbstu.preaccelerator.telegram.entities.state.URLMeeting
import ru.spbstu.preaccelerator.telegram.extensions.TrackerExt.teams
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings

fun StateMachineBuilder.addNewMeeting(){
    role<Tracker> {
        state<NewMeeting> {
            onTransition {
                sendTextMessage(
                    it,
                    MenuStrings.Tracker.ScheduleMeetings.ChooseTeam,
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
                var teamName = message.content.text
                setState(URLMeeting)
            }
        }
        state<URLMeeting> {
                onTransition {
                    sendTextMessage(
                        it,
                        MenuStrings.Tracker.ScheduleMeetings.InputURL
                    )
                }
                onText { message ->
                    var url = message.content.text
                    setState(TimeMeeting)
                }
        }
        state<TimeMeeting> {
            onTransition {
                sendTextMessage(
                    it,
                    MenuStrings.Tracker.ScheduleMeetings.InputTime
                )
            }
            onText { message ->
                var time = message.content.text
                setState(CheckCorrect)
            }
        }
//            onTransition {
//                sendTextMessage(
//                    it,
//                    MenuStrings.meetingCreationConfirmation(teamName,time,url),
//                    replyMarkup = replyKeyboard(
//                        resizeKeyboard=true,
//                        oneTimeKeyboard = true
//                    )
//                    {
//                        row {
//                            simpleButton(MessageStrings.Option.Yes)
//                            simpleButton(MessageStrings.Option.No)
//                        }
//                    }
//                )
//            }

        }
    }

