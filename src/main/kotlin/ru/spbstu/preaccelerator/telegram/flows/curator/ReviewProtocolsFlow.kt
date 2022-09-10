package ru.spbstu.preaccelerator.telegram.flows.curator

import com.ithersta.tgbotapi.fsm.entities.triggers.onDataCallbackQuery
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.answers.answer
import dev.inmo.tgbotapi.extensions.api.delete
import dev.inmo.tgbotapi.extensions.api.edit.text.editMessageText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.asMessageCallbackQuery
import dev.inmo.tgbotapi.extensions.utils.types.buttons.dataButton
import dev.inmo.tgbotapi.extensions.utils.types.buttons.inlineKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.row
import dev.inmo.tgbotapi.extensions.utils.withContent
import dev.inmo.tgbotapi.types.buttons.InlineKeyboardMarkup
import dev.inmo.tgbotapi.types.buttons.ReplyForce
import dev.inmo.tgbotapi.types.message.content.TextContent
import dev.inmo.tgbotapi.types.message.textsources.TextSourcesList
import dev.inmo.tgbotapi.utils.PreviewFeature
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.ProtocolStatus
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module
import ru.spbstu.preaccelerator.domain.entities.user.Curator
import ru.spbstu.preaccelerator.domain.repository.ProtocolStatusRepository
import ru.spbstu.preaccelerator.domain.repository.TeamRepository
import ru.spbstu.preaccelerator.telegram.RoleFilterBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState
import ru.spbstu.preaccelerator.telegram.entities.state.ReviewProtocolsState
import ru.spbstu.preaccelerator.telegram.extensions.TeamExt.protocol
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings

@OptIn(PreviewFeature::class)
fun RoleFilterBuilder<Curator>.reviewProtocolsFlow() {
    val teamRepository: TeamRepository by inject()
    val protocolStatusRepository: ProtocolStatusRepository by inject()
    anyState {
        onDataCallbackQuery(Regex("protocol (decline|accept) \\d+ \\d+")) {
            val tokens = it.data.split(" ")
            val newStatus = when (tokens[1]) {
                "decline" -> ProtocolStatus.Value.Declined
                "accept" -> ProtocolStatus.Value.Accepted
                else -> return@onDataCallbackQuery
            }
            val teamId = Team.Id(tokens[2].toLong())
            val moduleNumber = Module.Number(tokens[3].toInt())
            val team = teamRepository.get(teamId)
            val protocolStatus = protocolStatusRepository.set(teamId, moduleNumber, newStatus)
            val message = it.asMessageCallbackQuery()?.message?.withContent<TextContent>() ?: return@onDataCallbackQuery
            editMessageText(
                message,
                text(team, protocolStatus),
                replyMarkup = keyboard(protocolStatus),
                disableWebPagePreview = true
            )
            if (protocolStatus.value == ProtocolStatus.Value.Declined) {
                setState(ReviewProtocolsState.WaitingForComment(teamId, moduleNumber, message.messageId))
            } else {
                setState(ReviewProtocolsState.New)
            }
            answer(it)
        }
    }
    state<ReviewProtocolsState.New> {
        onTransition {
            val protocolStatus = protocolStatusRepository.getSent() ?: run {
                sendTextMessage(it, MessageStrings.ReviewProtocols.NoUnreviewedProtocols)
                setState(EmptyState)
                return@onTransition
            }
            val team = teamRepository.get(protocolStatus.teamId)
            sendTextMessage(it, text(team, protocolStatus), replyMarkup = keyboard(protocolStatus))
        }
    }
    state<ReviewProtocolsState.WaitingForComment> {
        onTransition {
            sendTextMessage(
                it,
                MessageStrings.ReviewProtocols.SendComment,
                replyMarkup = ReplyForce(inputFieldPlaceholder = MessageStrings.ReviewProtocols.SendCommentPlaceholder)
            )
        }
        onText { message ->
            val team = teamRepository.get(state.teamId)
            val protocolStatus = protocolStatusRepository.set(state.teamId, state.moduleNumber, message.content.text)
            editMessageText(
                message.chat,
                state.messageId,
                text(team, protocolStatus),
                replyMarkup = keyboard(protocolStatus),
                disableWebPagePreview = true
            )
            delete(message)
            message.replyTo?.let { delete(it) }
            setState(ReviewProtocolsState.New)
        }
    }
}

private fun text(team: Team, protocolStatus: ProtocolStatus): TextSourcesList {
    val status = when (protocolStatus.value) {
        ProtocolStatus.Value.Accepted -> MessageStrings.ReviewProtocols.Accepted
        ProtocolStatus.Value.Declined -> MessageStrings.ReviewProtocols.Declined
        else -> null
    }
    return MessageStrings.ReviewProtocols.protocol(
        team.protocol,
        team,
        protocolStatus.moduleNumber,
        status,
        protocolStatus.comment
    )
}

private fun keyboard(protocolStatus: ProtocolStatus): InlineKeyboardMarkup = inlineKeyboard {
    if (protocolStatus.value == ProtocolStatus.Value.Sent) {
        row {
            dataButton(
                ButtonStrings.ReviewProtocols.Decline,
                "protocol decline ${protocolStatus.teamId.value} ${protocolStatus.moduleNumber.value}"
            )
            dataButton(
                ButtonStrings.ReviewProtocols.Accept,
                "protocol accept ${protocolStatus.teamId.value} ${protocolStatus.moduleNumber.value}"
            )
        }
    }
}
