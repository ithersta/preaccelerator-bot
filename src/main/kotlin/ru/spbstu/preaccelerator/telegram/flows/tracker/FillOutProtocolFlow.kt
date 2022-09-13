package ru.spbstu.preaccelerator.telegram.flows

import com.ithersta.tgbotapi.fsm.entities.triggers.onDataCallbackQuery
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.*
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.ProtocolStatus
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.isFinished
import ru.spbstu.preaccelerator.domain.entities.module.Module.Number
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.domain.repository.CuratorRepository
import ru.spbstu.preaccelerator.domain.repository.ProtocolRepository
import ru.spbstu.preaccelerator.domain.repository.ProtocolStatusRepository
import ru.spbstu.preaccelerator.domain.repository.TeamRepository
import ru.spbstu.preaccelerator.telegram.StateMachineBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState
import ru.spbstu.preaccelerator.telegram.entities.state.ProtocolState.*
import ru.spbstu.preaccelerator.telegram.extensions.TeamExt.availableModules
import ru.spbstu.preaccelerator.telegram.extensions.TrackerExt.teams
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.Attention
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.ChooseModule
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.ChooseTeam
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.InputGoogleDiskUrl
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.MessageCurator
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.ProtocolChanged
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.ProtocolHasBeenSent
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.ReadyCheck
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.ViewProtocol
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.confirmationProtocol
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.explanationReasons
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.map
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.textForCurator


fun StateMachineBuilder.fillOutProtocolFlow() {
    val protocolRepository: ProtocolRepository by inject()
    val statusRepository: ProtocolStatusRepository by inject()
    val teamRepository: TeamRepository by inject()
    val curatorRepository: CuratorRepository by inject()
    role<Tracker> {
        state<ChooseTeam> {
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
        state<ChooseModule> {
            onTransition { chatId ->
                sendTextMessage(chatId, ChooseModule, replyMarkup = inlineKeyboard {
                    teamRepository.get(state.teamId).availableModules.chunked(2).forEach {
                        row {
                            it.forEach {
                                val status = statusRepository.get(state.teamId, it.number)
                                dataButton(map[status.value] + it.name, "moduleId ${it.number.value}")
                            }
                        }
                    }
                })
            }
            onDataCallbackQuery(Regex("moduleId \\d+")) {
                val moduleNumber = Number(it.data.split(' ').last().toInt())
                protocolRepository.get(state.teamId)
                if (protocolRepository.get(state.teamId) == null) {
                    setState(SendDiskUrl(state.teamId, moduleNumber))
                } else {
                    setState(
                        ChooseProtocol(state.teamId, moduleNumber, protocolRepository.get(state.teamId))
                    )
                }
            }
        }
        state<SendDiskUrl> {
            onTransition { chatId ->
                sendTextMessage(chatId, InputGoogleDiskUrl)
                setState(ChooseProtocol(state.teamId, state.moduleNumber, protocolRepository.get(state.teamId)))
            }
        }
        state<ChooseProtocol> {
            onTransition {
                if (!statusRepository.get(state.teamId, state.moduleNumber).isFinished()) {
                    setState(NotificationButton(state.teamId, state.moduleNumber, state.protocol!!.url))
                } else {
                    val status = statusRepository.get(state.teamId, state.moduleNumber)
                    if (status.value == ProtocolStatus.Value.Declined) {
                        setState(FixWrongProtocol(state.teamId, state.moduleNumber))
                    }
                    sendTextMessage(it, ProtocolHasBeenSent)
                    setState(EmptyState)
                }
            }

        }

        state<FixWrongProtocol> {
            onTransition { chatId ->
                sendTextMessage(chatId, explanationReasons(
                    state.moduleNumber.value.toString(),
                    statusRepository.get(state.teamId, state.moduleNumber).comment.toString()
                ), replyMarkup = replyKeyboard {
                    row {
                        simpleButton(ProtocolChanged)
                    }
                })
            }
            onText { message ->
                statusRepository.set(state.teamId, state.moduleNumber, ProtocolStatus.Value.Sent)
                sendTextMessage(message.chat, ProtocolHasBeenSent)
                setState(EmptyState)
            }
        }
        state<NotificationButton> {
            onTransition { chatId ->
                sendTextMessage(chatId,
                    Attention,
                    replyMarkup = replyKeyboard(resizeKeyboard = true, oneTimeKeyboard = true) {
                        row {
                            simpleButton(MessageCurator)
                        }
                    })
            }
            onText(MessageCurator) { message ->
                sendTextMessage(message.chat, confirmationProtocol(state.moduleNumber.value.toString()))
                statusRepository.set(state.teamId, state.moduleNumber, ProtocolStatus.Value.Sent)
                curatorRepository.getCurators().map {
                    sendTextMessage(it.userId,
                        textForCurator(state.moduleNumber.value.toString(), teamRepository.get(state.teamId).name),
                        replyMarkup = inlineKeyboard { row { urlButton(ViewProtocol, state.urlOrProtocol) } })
                    sendTextMessage(
                        it.userId,
                        ReadyCheck,
                        replyMarkup = inlineKeyboard {
                            row {
                                dataButton(
                                    ButtonStrings.Option.Yes,
                                    "do review ${state.teamId} ${state.moduleNumber.value}"
                                )
                                dataButton(ButtonStrings.Option.No, " ")
                            }
                        })
                }
                setState(EmptyState)
            }
        }
    }
}