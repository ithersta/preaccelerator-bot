package ru.spbstu.preaccelerator.telegram.flows.tracker

import com.ithersta.tgbotapi.fsm.entities.triggers.onDataCallbackQuery
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import com.ithersta.tgbotapi.pagination.inlineKeyboardPager
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.*
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import dev.inmo.tgbotapi.types.message.MarkdownV2
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module
import ru.spbstu.preaccelerator.domain.entities.module.ModuleConfig
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.domain.repository.MeetingRepository
import ru.spbstu.preaccelerator.domain.repository.TeamRepository
import ru.spbstu.preaccelerator.telegram.RoleFilterBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.MenuState
import ru.spbstu.preaccelerator.telegram.entities.state.NewMeetingState
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun RoleFilterBuilder<Tracker>.addNewMeetingFlow() {
    val moduleConfig: ModuleConfig by inject()
    val meetingRepository: MeetingRepository by inject()
    val teamRepository: TeamRepository by inject()
    val zoneId: ZoneId by inject()
    state<NewMeetingState.WaitingForModuleNumber> {
        onTransition { chatId ->
            sendTextMessage(
                chatId,
                MessageStrings.ScheduleMeetings.InputModuleNumber,
                replyMarkup = ReplyKeyboardRemove()
            )
        }
        onText { message ->
            val moduleNumber = message.content.text.toIntOrNull()?.let { Module.Number(it) } ?: run {
                sendTextMessage(
                    message.chat,
                    MessageStrings.ScheduleMeetings.InvalidDataFormat + MessageStrings.ScheduleMeetings.InputModuleNumber,
                    parseMode = MarkdownV2
                )
                return@onText
            }
            if (moduleConfig.modules.containsKey(moduleNumber)) {
                setState(NewMeetingState.WaitingForTeam(moduleNumber))
            } else {
                sendTextMessage(
                    message.chat,
                    MessageStrings.ScheduleMeetings.InvalidModuleNumber,
                    parseMode = MarkdownV2
                )
                return@onText
            }
        }
    }
    val teamPager = inlineKeyboardPager("addNewMeetingFlow") { offset, limit ->
        val teams = teamRepository.getByTrackerIdPaginated(user.id, offset, limit)
        val count = teamRepository.countByTrackerId(user.id)
        inlineKeyboard {
            teams.forEach {
                row {
                    dataButton(it.name, "team ${it.id.value}")
                }
            }
            navigationRow(count)
        }
    }
    state<NewMeetingState.WaitingForTeam> {
        onTransition { chatId ->
            sendTextMessage(
                chatId,
                MessageStrings.ScheduleMeetings.ChooseTeam,
                replyMarkup = teamPager.firstPage
            )
        }
        onDataCallbackQuery(Regex("team \\d+")) {
            val teamId = Team.Id(it.data.split(" ").last().toLong())
            setState(NewMeetingState.WaitingForUrl(state.moduleNumber, teamId))
        }
    }
    state<NewMeetingState.WaitingForUrl> {
        onTransition { chatId ->
            sendTextMessage(
                chatId,
                MessageStrings.ScheduleMeetings.InputUrl
            )
        }
        onText { message ->
            val url = message.content.text
            setState(NewMeetingState.WaitingForDateTime(state.moduleNumber, state.teamId, url))
        }
    }
    state<NewMeetingState.WaitingForDateTime> {
        onTransition { chatId ->
            sendTextMessage(
                chatId,
                MessageStrings.ScheduleMeetings.InputDateTime,
                parseMode = MarkdownV2
            )
        }
        onText { message ->
            val dateTime = try {
                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").withZone(zoneId)
                ZonedDateTime.parse(message.content.text, formatter).toOffsetDateTime()
            } catch (e: DateTimeParseException) {
                sendTextMessage(
                    message.chat,
                    MessageStrings.ScheduleMeetings.InvalidDataFormat + MessageStrings.ScheduleMeetings.InputDateTime,
                    parseMode = MarkdownV2
                )
                return@onText
            }
            setState(NewMeetingState.CheckCorrect(state.moduleNumber, state.teamId, state.url, dateTime))
        }
    }
    state<NewMeetingState.CheckCorrect> {
        onTransition { chatId ->
            sendTextMessage(
                chatId,
                MessageStrings.meetingCreationConfirmation(
                    teamRepository.get(state.teamId).name,
                    state.dateTime,
                    state.url
                ),
                replyMarkup = replyKeyboard(
                    resizeKeyboard = true,
                    oneTimeKeyboard = true
                ) {
                    row {
                        simpleButton(ButtonStrings.Option.Yes)
                        simpleButton(ButtonStrings.Option.No)
                    }
                }
            )
        }
        onText(ButtonStrings.Option.Yes) { message ->
            sendTextMessage(
                message.chat,
                MessageStrings.ScheduleMeetings.MeetingIsCreated,
                parseMode = MarkdownV2
            )
            meetingRepository.add(state.teamId, state.moduleNumber, state.dateTime, state.url)
            setState(MenuState.Tracker.Meetings)
        }
        onText(ButtonStrings.Option.No) { message ->
            sendTextMessage(
                message.chat,
                MessageStrings.ScheduleMeetings.MeetingNotCreated,
                parseMode = MarkdownV2
            )
            setState(NewMeetingState.WaitingForModuleNumber)
        }
    }
}
