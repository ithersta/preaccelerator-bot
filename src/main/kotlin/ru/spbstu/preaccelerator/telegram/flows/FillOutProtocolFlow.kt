package ru.spbstu.preaccelerator.telegram.flows

import com.ithersta.tgbotapi.fsm.entities.triggers.onDataCallbackQuery
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.*
import ru.spbstu.preaccelerator.domain.entities.ProtocolStatus
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module.Number
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.telegram.StateMachineBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.ProtocolState.*
import ru.spbstu.preaccelerator.telegram.extensions.TeamExt.availableModules
import ru.spbstu.preaccelerator.telegram.extensions.TeamExt.getProtocolStatus
import ru.spbstu.preaccelerator.telegram.extensions.TeamExt.protocol
import ru.spbstu.preaccelerator.telegram.extensions.TeamExt.setProtocolStatus
import ru.spbstu.preaccelerator.telegram.extensions.TrackerExt.teams
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.ChooseModule
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.ChooseProtocol
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.ChooseTeam
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.InputGoogleDiskUrl
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.MessageCurator
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.confirmationProtocol

fun StateMachineBuilder.fillOutProtocolFlow() {
    role<Tracker> {
        state<ChooseTeam> {
//      команды
            onTransition { chatId ->
                sendTextMessage(chatId, ChooseTeam, replyMarkup = inlineKeyboard {
                    user.teams.chunked(2).forEach {
                        row {
                            it.forEach { dataButton(it.name, "teamId ${it.id.value}") }
                        }
                    }
                })
            }
            onDataCallbackQuery(Regex("teamId \\d+")) {
                val teamId = Team.Id(it.data.split(" ").last().toLong())
                setState(ChooseModule(teamId))
            }
        }
//      модули
        state<ChooseModule> {
            onTransition { chatId ->
                sendTextMessage(chatId, ChooseModule, replyMarkup = inlineKeyboard {
                    user.teams[state.teamId.value.toInt()].availableModules.chunked(2).forEach {
                        row {
                            it.forEach {
                                dataButton(it.name, "moduleId ${it.number.value}")
                            }
                        }
                    }
                })
            }
            onDataCallbackQuery(Regex("moduleId \\d+")) {
                val moduleNumber = Number(it.data.split(' ').last().toInt())
                if (user.teams[state.teamId.value.toInt()].protocol == null) {
                    setState(SendDiskUrl(state.teamId, moduleNumber))
                } else {
                    setState(
                        ChooseProtocol(state.teamId, moduleNumber, user.teams[state.teamId.value.toInt()].protocol)
                    )
                }
            }
        }
//      ссылка
        state<SendDiskUrl> {
            onTransition { chatId ->
                sendTextMessage(chatId, InputGoogleDiskUrl)
            }
            onText { message ->
                val googleDiskLink = message.content.text
                if (user.teams[state.teamId.value.toInt()].getProtocolStatus(state.moduleNumber).value == ProtocolStatus.Value.Unsent) {
                    setState(NotificationButton(state.teamId, state.moduleNumber, googleDiskLink))
                }
            }
        }
//      если протокол есть
        state<ChooseProtocol> {
            onTransition { chatId ->
                sendTextMessage(chatId,
                    ChooseProtocol,
                    replyMarkup = replyKeyboard(resizeKeyboard = true, oneTimeKeyboard = true) {
                        row {
                            simpleButton(state.protocol!!.url)
                        }
                    })
            }
            onText { message ->
                val protocol = message.content.text
                if (user.teams[state.teamId.value.toInt()].getProtocolStatus(state.moduleNumber).value == ProtocolStatus.Value.Unsent) {
                    setState(NotificationButton(state.teamId, state.moduleNumber, protocol))
                }
            }
        }

//      кнопка для отправки оповещения куратору
        state<NotificationButton> {
            onTransition { chatId ->
                sendTextMessage(chatId,
                    String(),
                    replyMarkup = replyKeyboard(resizeKeyboard = true, oneTimeKeyboard = true) {
                        row {
                            simpleButton(MessageCurator)
                        }
                    })
            }
            onText { message ->
                sendTextMessage(message.chat, confirmationProtocol(state.moduleNumber.value.toString()))
                user.teams[state.teamId.value.toInt()].setProtocolStatus(state.moduleNumber, ProtocolStatus.Value.Sent)
            }
        }
    }
}