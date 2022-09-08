package ru.spbstu.preaccelerator.telegram.flows


import com.ithersta.tgbotapi.fsm.entities.triggers.onDataCallbackQuery
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.edit.reply_markup.editMessageReplyMarkup
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.messageCallbackQueryOrThrow
import dev.inmo.tgbotapi.extensions.utils.types.buttons.dataButton
import dev.inmo.tgbotapi.extensions.utils.types.buttons.inlineKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.row
import dev.inmo.tgbotapi.types.UserId
import dev.inmo.tgbotapi.types.buttons.InlineKeyboardMarkup
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.user.Curator
import ru.spbstu.preaccelerator.domain.entities.user.PreacceleratorUser
import ru.spbstu.preaccelerator.domain.repository.TeamRepository
import ru.spbstu.preaccelerator.telegram.RoleFilterBuilder
import ru.spbstu.preaccelerator.telegram.StateFilterBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.DialogState
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState
import ru.spbstu.preaccelerator.telegram.entities.state.SendInfoState
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings

fun RoleFilterBuilder<Curator>.sendingInfo() {
    val teamRepository: TeamRepository by inject()
    state<SendInfoState.BaseState> {
        val teams = teamRepository.getAll()
        val setTeams = mutableSetOf<Team.Id>()
        var infoMass = ""
        var startInputMass = false
        onTransition { user ->
            if (state is SendInfoState.TeamsFromCurator) {
                sendTextMessage(
                    chatId = user,
                    text = MessageStrings.Curator.massSendInfo.listOfTeams,
                    replyMarkup = inlineTeams(teams, setTeams)
                )
            } else {
                sendTextMessage(
                    chatId = user,
                    text = MessageStrings.Curator.massSendInfo.inputOneMessage,
                    replyMarkup = ReplyKeyboardRemove()
                )
                startInputMass = true
            }
        }
        onText { message ->
            if (startInputMass) {
                infoMass = message.content.text
                sendTextMessage(
                    chat = message.chat,
                    text = MessageStrings.Curator.massSendInfo.requestConfirm(infoMass),
                    replyMarkup = inlineKeyboard {
                        row {
                            dataButton(MessageStrings.Curator.massSendInfo.send, "send info")
                        }
                        row {
                            dataButton(MessageStrings.Curator.massSendInfo.inputOtherMessage, "input other message")
                        }
                    }
                )
            }
        }
        onDataCallbackQuery(Regex("send info")) { message ->
            when(state){
                SendInfoState.AllFromCurator -> sendTextMessage(message.from, "TODO: рассылка всем")
                SendInfoState.TeamsFromCurator -> sendTextMessage(message.from, "TODO: рассылка по командам")
                SendInfoState.TrackersFromCurator -> sendTextMessage(message.from, "TODO: рассылка всем трекерам")
            }
            editMessageReplyMarkup(
                chat = message.from,
                messageId = message.messageCallbackQueryOrThrow().message.messageId,
                replyMarkup = null
            )
            setState(EmptyState)
        }
        onDataCallbackQuery(Regex("input other message")) { message ->
            editMessageReplyMarkup(
                chat = message.from,
                messageId = message.messageCallbackQueryOrThrow().message.messageId,
                replyMarkup = null
            )
            sendTextMessage(
                chat = message.from,
                text = MessageStrings.Curator.massSendInfo.inputOneMessage,
                replyMarkup = ReplyKeyboardRemove()
            )
        }
        onDataCallbackQuery(Regex("add:\\d+")) { message ->
            setTeams.add(Team.Id(message.data.split(":")[1].toLong()))
            editMessageReplyMarkup(
                chat = message.from,
                messageId = message.messageCallbackQueryOrThrow().message.messageId,
                replyMarkup = inlineTeams(teams, setTeams)
            )
        }
        onDataCallbackQuery(Regex("del:\\d+")) { message ->
            setTeams.remove(Team.Id(message.data.split(":")[1].toLong()))
            editMessageReplyMarkup(
                chat = message.from,
                messageId = message.messageCallbackQueryOrThrow().message.messageId,
                replyMarkup = inlineTeams(teams, setTeams)
            )
        }
        onDataCallbackQuery(Regex("send such teams")) { message ->
            editMessageReplyMarkup(
                chat = message.from,
                messageId = message.messageCallbackQueryOrThrow().message.messageId,
                replyMarkup = null
            )
            sendTextMessage(
                chat = message.from,
                text = MessageStrings.Curator.massSendInfo.inputOneMessage,
                replyMarkup = ReplyKeyboardRemove()
            )
            startInputMass = true
        }
    }
}

private fun inlineTeams(teams: List<Team>, setTeams: Set<Team.Id>): InlineKeyboardMarkup{
    return inlineKeyboard {
        teams.chunked(2).forEach { chunk ->
            row {
                chunk.forEach { team ->
                    if (setTeams.contains(team.id)){
                        dataButton("-${team.name}", "del:${team.id.value}")
                    } else {
                        dataButton("+${team.name}", "add:${team.id.value}")
                    }
                }
            }
        }
        row {
            dataButton(MessageStrings.Curator.massSendInfo.sendSuchTeams, "send such teams")
        }
    }
}