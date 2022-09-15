package ru.spbstu.preaccelerator.telegram.flows.tracker

import com.ithersta.tgbotapi.fsm.entities.triggers.dataButton
import com.ithersta.tgbotapi.fsm.entities.triggers.onDataCallbackQuery
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import com.ithersta.tgbotapi.pagination.inlineKeyboardPager
import dev.inmo.tgbotapi.extensions.api.edit.reply_markup.editMessageReplyMarkup
import dev.inmo.tgbotapi.extensions.api.edit.text.editMessageText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.messageCallbackQueryOrThrow
import dev.inmo.tgbotapi.extensions.utils.types.buttons.*
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import org.apache.commons.validator.routines.UrlValidator
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.ProtocolStatus
import ru.spbstu.preaccelerator.domain.entities.isFinished
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.domain.repository.CuratorRepository
import ru.spbstu.preaccelerator.domain.repository.ProtocolRepository
import ru.spbstu.preaccelerator.domain.repository.ProtocolStatusRepository
import ru.spbstu.preaccelerator.domain.repository.TeamRepository
import ru.spbstu.preaccelerator.telegram.RoleFilterBuilder
import ru.spbstu.preaccelerator.telegram.entities.query.FillOutProtocolQuery
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState
import ru.spbstu.preaccelerator.telegram.entities.state.ProtocolState
import ru.spbstu.preaccelerator.telegram.extensions.TeamExt.availableModules
import ru.spbstu.preaccelerator.telegram.flows.curator.declineOrAcceptKeyboard
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.ChooseTeam
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.InputGoogleDiskUrl
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.InvalidProtocolUrl
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.MarkAsSentQuestion
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.MessageCurator
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.ProtocolChanged
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.ProtocolHasBeenSent
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.ReadyCheck
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.ViewProtocol
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.chooseModule
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.confirmationProtocol
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.emoji
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.explanationReasons
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.textForCurator

fun RoleFilterBuilder<Tracker>.fillOutProtocolFlow() {
    val protocolRepository: ProtocolRepository by inject()
    val protocolStatusRepository: ProtocolStatusRepository by inject()
    val teamRepository: TeamRepository by inject()
    val curatorRepository: CuratorRepository by inject()

    val teamPager = inlineKeyboardPager("fillOutProtocolFlow") { offset, limit ->
        val teams = teamRepository.getByTrackerIdPaginated(user.id, offset, limit)
        val count = teamRepository.countByTrackerId(user.id)
        inlineKeyboard {
            teams.forEach {
                row {
                    dataButton(it.name, FillOutProtocolQuery.OnTeamClicked(it.id))
                }
            }
            navigationRow(count)
        }
    }
    anyState {
        onDataCallbackQuery(FillOutProtocolQuery.OnTeamClicked::class) { (data, query) ->
            setState(ProtocolState.ChooseModule(data.teamId, query.messageCallbackQueryOrThrow().message.messageId))
        }
        onDataCallbackQuery(FillOutProtocolQuery.OnModuleClicked::class) { (data, query) ->
            if (protocolRepository.get(data.teamId) == null) {
                setState(ProtocolState.WaitingForUrl(data.teamId, data.moduleNumber, state))
            } else {
                val status = protocolStatusRepository.get(data.teamId, data.moduleNumber)
                if (!status.isFinished()) {
                    if (status.value == ProtocolStatus.Value.Declined) {
                        setState(ProtocolState.FixWrong(data.teamId, data.moduleNumber))
                    } else {
                        setState(
                            ProtocolState.NotificationButton(
                                data.teamId,
                                data.moduleNumber,
                                protocolRepository.get(data.teamId)!!.url
                            )
                        )
                    }
                } else {
                    sendTextMessage(query.from, ProtocolHasBeenSent)
                }
            }
        }
    }
    state<ProtocolState.ChooseTeam> {
        onTransition { chatId ->
            state.messageId?.let {
                runCatching { editMessageReplyMarkup(chatId, it, replyMarkup = teamPager.firstPage) }
            } ?: run {
                sendTextMessage(chatId, ChooseTeam, replyMarkup = teamPager.firstPage)
            }
        }
    }
    state<ProtocolState.ChooseModule> {
        onTransition { chatId ->
            val team = teamRepository.get(state.teamId)
            val replyMarkup = inlineKeyboard {
                teamRepository.get(state.teamId).availableModules.chunked(2).forEach {
                    row {
                        it.forEach {
                            val status = protocolStatusRepository.get(state.teamId, it.number)
                            dataButton(
                                "${status.value.emoji} ${it.name}",
                                FillOutProtocolQuery.OnModuleClicked(state.teamId, it.number)
                            )
                        }
                    }
                }
            }
            state.messageId?.let {
                runCatching { editMessageText(chatId, it, chooseModule(team.name), replyMarkup = replyMarkup) }
            } ?: run {
                sendTextMessage(chatId, chooseModule(team.name), replyMarkup = replyMarkup)
            }
        }
    }
    state<ProtocolState.WaitingForUrl> {
        onTransition {
            sendTextMessage(it, InputGoogleDiskUrl, replyMarkup = ReplyKeyboardRemove())
        }
        onText { message ->
            val protocolUrl = message.content.text.takeIf { UrlValidator().isValid(it) } ?: run {
                sendTextMessage(message.chat, InvalidProtocolUrl)
                return@onText
            }
            if (!protocolStatusRepository.get(state.teamId, state.moduleNumber).isFinished()) {
                protocolRepository.add(state.teamId, protocolUrl)
                setState(ProtocolState.NotificationButton(state.teamId, state.moduleNumber, protocolUrl))
            } else {
                sendTextMessage(message.chat, ProtocolHasBeenSent)
                setState(state.returnTo)
            }
        }
    }
    state<ProtocolState.FixWrong> {
        onTransition { chatId ->
            sendTextMessage(
                chatId,
                explanationReasons(
                    protocolStatusRepository.get(state.teamId, state.moduleNumber),
                    teamRepository.get(state.teamId),
                    protocolRepository.get(state.teamId)!!
                ),
                replyMarkup = replyKeyboard(resizeKeyboard = true) {
                    row {
                        simpleButton(ProtocolChanged)
                    }
                }
            )
        }

        onText(ProtocolChanged) { message ->
            protocolStatusRepository.set(state.teamId, state.moduleNumber, ProtocolStatus.Value.Sent)
            sendTextMessage(message.chat, ProtocolHasBeenSent)
            setState(EmptyState)
        }
    }
    state<ProtocolState.NotificationButton> {
        onTransition { chatId ->
            sendTextMessage(chatId,
                MarkAsSentQuestion,
                replyMarkup = replyKeyboard(resizeKeyboard = true, oneTimeKeyboard = true) {
                    row {
                        simpleButton(MessageCurator)
                    }
                })
        }
        onText(MessageCurator) { message ->
            sendTextMessage(message.chat, confirmationProtocol(state.moduleNumber.value.toString()))
            protocolStatusRepository.set(state.teamId, state.moduleNumber, ProtocolStatus.Value.Sent)
            curatorRepository.getAll().forEach {
                sendTextMessage(it.userId,
                    textForCurator(state.moduleNumber.value.toString(), teamRepository.get(state.teamId).name),
                    replyMarkup = inlineKeyboard { row { urlButton(ViewProtocol, state.url) } })
                sendTextMessage(
                    it.userId,
                    ReadyCheck,
                    replyMarkup = declineOrAcceptKeyboard(
                        protocolStatusRepository.get(
                            state.teamId,
                            state.moduleNumber
                        )
                    )
                )
            }
            setState(EmptyState)
        }
    }
}
