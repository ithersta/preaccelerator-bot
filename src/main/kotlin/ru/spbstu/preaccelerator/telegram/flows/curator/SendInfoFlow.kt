package ru.spbstu.preaccelerator.telegram.flows.curator

import com.ithersta.tgbotapi.fsm.entities.triggers.onDataCallbackQuery
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import com.ithersta.tgbotapi.pagination.statefulInlineKeyboardPager
import dev.inmo.tgbotapi.bot.RequestsExecutor
import dev.inmo.tgbotapi.extensions.api.answers.answer
import dev.inmo.tgbotapi.extensions.api.edit.text.editMessageText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.asMessageCallbackQuery
import dev.inmo.tgbotapi.extensions.utils.types.buttons.*
import dev.inmo.tgbotapi.extensions.utils.withContent
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import dev.inmo.tgbotapi.types.message.content.TextContent
import dev.inmo.tgbotapi.utils.PreviewFeature
import org.koin.core.annotation.Single
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.user.Curator
import ru.spbstu.preaccelerator.domain.entities.user.PreacceleratorUser
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.domain.repository.MemberRepository
import ru.spbstu.preaccelerator.domain.repository.TeamRepository
import ru.spbstu.preaccelerator.domain.repository.TrackerRepository
import ru.spbstu.preaccelerator.telegram.StateMachineBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState
import ru.spbstu.preaccelerator.telegram.entities.state.Recipient
import ru.spbstu.preaccelerator.telegram.entities.state.SendInfoState
import ru.spbstu.preaccelerator.telegram.extensions.MemberExt.userId
import ru.spbstu.preaccelerator.telegram.extensions.TeamExt.members
import ru.spbstu.preaccelerator.telegram.extensions.TrackerExt.teams
import ru.spbstu.preaccelerator.telegram.extensions.TrackerExt.userId
import ru.spbstu.preaccelerator.telegram.notifications.MassSendLimiter
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.SendInfo

@OptIn(PreviewFeature::class)
fun StateMachineBuilder.sendInfoFlow() {
    val teamRepository: TeamRepository by inject()
    val massSender: MassSender by inject()
    anyRole {
        state<SendInfoState.ChooseTeams> {
            val teamPager = statefulInlineKeyboardPager("sendInfoFlow",
                onPagerStateChanged = { state.copy(pagerState = it) }
            ) {
                val (teams, count) = when (val user = user) {
                    is Curator -> teamRepository.getAllPaginated(offset, limit) to teamRepository.countAll()
                    is Tracker -> teamRepository.getByTrackerIdPaginated(user.id, offset, limit) to
                            teamRepository.countByTrackerId(user.id)
                    else -> error("Only curators and trackers can use this feature")
                }
                inlineKeyboard {
                    teams.forEach {
                        row {
                            if (it.id in state.teamIds) {
                                dataButton(ButtonStrings.selected(it.name), "team unselect ${it.id.value}")
                            } else {
                                dataButton(ButtonStrings.unselected(it.name), "team select ${it.id.value}")
                            }
                        }
                    }
                    navigationRow(count)
                    row {
                        dataButton(ButtonStrings.SendInfo.FinishChoosingTeams, "finish")
                    }
                }
            }
            onTransition {
                teamPager.sendOrEditMessage(it, SendInfo.ChooseTeams, state.pagerState)
            }
            onDataCallbackQuery(Regex("team (select|unselect) \\d+")) {
                val tokens = it.data.split(" ")
                val teamId = Team.Id(tokens[2].toLong())
                val teamIds = when (tokens[1]) {
                    "select" -> state.teamIds + teamId
                    "unselect" -> state.teamIds - teamId
                    else -> error("Unknown action")
                }
                setState(state.copy(teamIds = teamIds))
                answer(it)
            }
            onDataCallbackQuery(Regex("finish")) { query ->
                val message =
                    query.asMessageCallbackQuery()?.message?.withContent<TextContent>() ?: return@onDataCallbackQuery
                val teams = state.teamIds.map { teamRepository.get(it) }
                editMessageText(message, text = SendInfo.chosenTeams(teams), replyMarkup = inlineKeyboard { })
                setState(SendInfoState.WaitingForMessage(Recipient.Teams(state.teamIds)))
                answer(query)
            }
        }
        state<SendInfoState.WaitingForMessage> {
            onTransition {
                sendTextMessage(it, SendInfo.EnterMessage, replyMarkup = ReplyKeyboardRemove())
            }
            onText {
                setState(SendInfoState.WaitingForConfirmation(state.recipient, it.content.text))
            }
        }
        state<SendInfoState.WaitingForConfirmation> {
            onTransition {
                sendTextMessage(
                    it,
                    SendInfo.confirmation(state.message),
                    replyMarkup = flatReplyKeyboard(resizeKeyboard = true) {
                        simpleButton(ButtonStrings.SendInfo.ChangeMessage)
                        simpleButton(ButtonStrings.SendInfo.Confirm)
                    })
            }
            onText(ButtonStrings.SendInfo.ChangeMessage) {
                setState(SendInfoState.WaitingForMessage(state.recipient))
            }
            onText(ButtonStrings.SendInfo.Confirm) {
                sendTextMessage(it.chat, SendInfo.Started)
                setState(EmptyState)
                val count = with(massSender) { send(user, state.recipient, state.message) }
                sendTextMessage(it.chat, SendInfo.success(count))
            }
        }
    }
}

@Single
class MassSender(
    private val memberRepository: MemberRepository,
    private val trackerRepository: TrackerRepository,
    private val teamRepository: TeamRepository,
    private val massSendLimiter: MassSendLimiter
) {
    suspend fun RequestsExecutor.send(from: PreacceleratorUser, recipient: Recipient, message: String): Int {
        val userIds = when (recipient) {
            Recipient.All -> memberRepository.getAll().mapNotNull { it.userId } +
                    trackerRepository.getAll().mapNotNull { it.userId }

            Recipient.AllFromTracker -> (from as Tracker).teams
                .flatMap { it.members }
                .mapNotNull { it.userId }

            Recipient.Trackers -> trackerRepository.getAll()
                .mapNotNull { it.userId }

            is Recipient.Teams -> recipient.ids
                .map { teamRepository.get(it) }
                .flatMap { it.members }
                .mapNotNull { it.userId }
        }
        val formattedMessage = when (from) {
            is Curator -> SendInfo.messageFromCurator(message)
            is Tracker -> SendInfo.messageFromTracker(message)
            else -> error("Only curators and trackers can use this feature")
        }
        return userIds.count {
            runCatching {
                massSendLimiter.wait()
                sendTextMessage(it, formattedMessage)
            }.isSuccess
        }
    }
}
