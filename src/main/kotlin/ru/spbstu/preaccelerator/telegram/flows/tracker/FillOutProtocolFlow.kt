package ru.spbstu.preaccelerator.telegram.flows.tracker

import com.ithersta.tgbotapi.fsm.entities.triggers.dataButton
import com.ithersta.tgbotapi.fsm.entities.triggers.onDataCallbackQuery
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import com.ithersta.tgbotapi.pagination.inlineKeyboardPager
import dev.inmo.tgbotapi.extensions.api.answers.answer
import dev.inmo.tgbotapi.extensions.api.edit.text.editMessageText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.messageCallbackQueryOrThrow
import dev.inmo.tgbotapi.extensions.utils.types.buttons.flatReplyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.inlineKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.row
import dev.inmo.tgbotapi.extensions.utils.types.buttons.simpleButton
import dev.inmo.tgbotapi.extensions.utils.withContentOrThrow
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import org.apache.commons.validator.routines.UrlValidator
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.ProtocolStatus
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.isFinished
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.domain.repository.CuratorRepository
import ru.spbstu.preaccelerator.domain.repository.ProtocolRepository
import ru.spbstu.preaccelerator.domain.repository.ProtocolStatusRepository
import ru.spbstu.preaccelerator.domain.repository.TeamRepository
import ru.spbstu.preaccelerator.telegram.RoleFilterBuilder
import ru.spbstu.preaccelerator.telegram.entities.query.FillOutProtocolQuery
import ru.spbstu.preaccelerator.telegram.entities.state.FillOutProtocolState
import ru.spbstu.preaccelerator.telegram.entities.state.MenuState
import ru.spbstu.preaccelerator.telegram.extensions.TeamExt.availableModules
import ru.spbstu.preaccelerator.telegram.flows.curator.declineOrAcceptKeyboard
import ru.spbstu.preaccelerator.telegram.flows.curator.reviewProtocolText
import ru.spbstu.preaccelerator.telegram.notifications.MassSendLimiter
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.ChooseTeam
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.InputGoogleDiskUrl
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.InvalidProtocolUrl
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.MarkAsSentQuestion
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.NewProtocol
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.ProtocolHasBeenSent
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.chooseModule
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.confirmationProtocol
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.emoji
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.Tracker.explanationReasons

fun RoleFilterBuilder<Tracker>.fillOutProtocolFlow() {
    val protocolRepository: ProtocolRepository by inject()
    val protocolStatusRepository: ProtocolStatusRepository by inject()
    val teamRepository: TeamRepository by inject()
    val curatorRepository: CuratorRepository by inject()
    val massSendLimiter: MassSendLimiter by inject()

    val teamPager = inlineKeyboardPager("fillOutProtocolFlow") {
        val teams = teamRepository.getByTrackerIdPaginated(user.id, offset, limit)
        val count = teamRepository.countByTrackerId(user.id)
        inlineKeyboard {
            teams.forEach {
                row {
                    dataButton(it.name, FillOutProtocolQuery.OnTeamClicked(page, it.id))
                }
            }
            navigationRow(count)
        }
    }

    fun protocolsKeyboard(page: Int, teamId: Team.Id) = inlineKeyboard {
        row {
            dataButton(MenuStrings.Back, FillOutProtocolQuery.BackToTeams(page))
        }
        teamRepository.get(teamId).availableModules.chunked(2).forEach {
            row {
                it.forEach {
                    val status = protocolStatusRepository.get(teamId, it.number)
                    dataButton(
                        "${status.value.emoji} ${it.name}",
                        FillOutProtocolQuery.OnModuleClicked(page, teamId, it.number)
                    )
                }
            }
        }
    }

    anyState {
        onDataCallbackQuery(FillOutProtocolQuery.BackToTeams::class) { (data, query) ->
            runCatching {
                editMessageText(
                    query.messageCallbackQueryOrThrow().message.withContentOrThrow(),
                    ChooseTeam,
                    replyMarkup = teamPager.page(data.page)
                )
            }
            answer(query)
        }
        onDataCallbackQuery(FillOutProtocolQuery.OnTeamClicked::class) { (data, query) ->
            val team = teamRepository.get(data.teamId)
            runCatching {
                editMessageText(
                    query.messageCallbackQueryOrThrow().message.withContentOrThrow(),
                    chooseModule(team.name),
                    replyMarkup = protocolsKeyboard(data.page, data.teamId)
                )
            }
            answer(query)
        }
        onDataCallbackQuery(FillOutProtocolQuery.OnModuleClicked::class) { (data, query) ->
            val messageId = query.messageCallbackQueryOrThrow().message.messageId
            if (protocolRepository.get(data.teamId) == null) {
                setState(FillOutProtocolState.WaitingForUrl(data.teamId, data.moduleNumber, messageId, data.page, state))
                answer(query)
            } else {
                val status = protocolStatusRepository.get(data.teamId, data.moduleNumber)
                if (!status.isFinished()) {
                    setState(FillOutProtocolState.WaitingForConfirmation(data.teamId, data.moduleNumber, messageId, data.page, state))
                    answer(query)
                } else {
                    answer(query, text = ProtocolHasBeenSent)
                }
            }
        }
    }

    state<FillOutProtocolState.ChooseTeam> {
        onTransition { chatId ->
            sendTextMessage(chatId, ChooseTeam, replyMarkup = teamPager.firstPage)
            setStateQuiet(MenuState.Tracker.Meetings)
        }
    }

    state<FillOutProtocolState.WaitingForUrl> {
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
                setState(FillOutProtocolState.WaitingForConfirmation(state.teamId, state.moduleNumber, state.messageId, state.page, state.returnTo))
            } else {
                sendTextMessage(message.chat, ProtocolHasBeenSent)
            }
        }
    }

    state<FillOutProtocolState.WaitingForConfirmation> {
        onTransition { chatId ->
            val protocolStatus = protocolStatusRepository.get(state.teamId, state.moduleNumber)
            if (protocolStatus.value == ProtocolStatus.Value.Declined) {
                sendTextMessage(
                    chatId,
                    explanationReasons(
                        protocolStatus,
                        teamRepository.get(state.teamId),
                        protocolRepository.get(state.teamId)!!
                    ),
                    replyMarkup = flatReplyKeyboard(resizeKeyboard = true) {
                        simpleButton(ButtonStrings.Cancel)
                        simpleButton(ButtonStrings.FillOutProtocol.Fixed)
                    }
                )
            } else {
                sendTextMessage(
                    chatId,
                    MarkAsSentQuestion,
                    replyMarkup = flatReplyKeyboard(resizeKeyboard = true, oneTimeKeyboard = true) {
                        simpleButton(ButtonStrings.Cancel)
                        simpleButton(ButtonStrings.FillOutProtocol.MarkAsSent)
                    }
                )
            }
        }
        onText(ButtonStrings.Cancel) {
            setState(state.returnTo)
        }
        onText(ButtonStrings.FillOutProtocol.MarkAsSent, ButtonStrings.FillOutProtocol.Fixed) { message ->
            sendTextMessage(message.chat, confirmationProtocol(state.moduleNumber.value.toString()))
            val team = teamRepository.get(state.teamId)
            val protocolStatus = protocolStatusRepository.set(state.teamId, state.moduleNumber, ProtocolStatus.Value.Sent)
            runCatching {
                editMessageText(
                    message.chat,
                    state.messageId,
                    chooseModule(team.name),
                    replyMarkup = protocolsKeyboard(state.page, state.teamId)
                )
            }
            setState(state.returnTo)
            curatorRepository.getAll().forEach {
                runCatching {
                    massSendLimiter.wait()
                    sendTextMessage(
                        it.userId,
                        reviewProtocolText(team, protocolStatus, NewProtocol),
                        replyMarkup = declineOrAcceptKeyboard(protocolStatus)
                    )
                }
            }
        }
    }
}
