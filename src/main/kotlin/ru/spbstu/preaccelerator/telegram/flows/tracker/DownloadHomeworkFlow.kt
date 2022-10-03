package ru.spbstu.preaccelerator.telegram.flows.tracker

import com.ithersta.tgbotapi.fsm.entities.triggers.dataButton
import com.ithersta.tgbotapi.fsm.entities.triggers.onDataCallbackQuery
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import com.ithersta.tgbotapi.pagination.inlineKeyboardPager
import dev.inmo.tgbotapi.extensions.api.answers.answer
import dev.inmo.tgbotapi.extensions.api.edit.text.editMessageText
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.messageCallbackQueryOrThrow
import dev.inmo.tgbotapi.extensions.utils.types.buttons.inlineKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.urlButton
import dev.inmo.tgbotapi.extensions.utils.withContentOrThrow
import dev.inmo.tgbotapi.utils.row
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.module.ModuleConfig
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.domain.repository.HomeworkRepository
import ru.spbstu.preaccelerator.domain.repository.TeamRepository
import ru.spbstu.preaccelerator.telegram.RoleFilterBuilder
import ru.spbstu.preaccelerator.telegram.entities.query.DownloadHomeworkQuery
import ru.spbstu.preaccelerator.telegram.entities.state.DownloadHomeworkState
import ru.spbstu.preaccelerator.telegram.entities.state.MenuState
import ru.spbstu.preaccelerator.telegram.extensions.TeamExt.availableModules
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.DownloadHomework.ChooseTeam
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.DownloadHomework.NoHomeworksDone
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.DownloadHomework.chooseModuleNumber
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.DownloadHomework.moduleHomeworks

fun RoleFilterBuilder<Tracker>.downloadHomeworkFlow() {
    val homeworkRepository: HomeworkRepository by inject()
    val moduleConfig: ModuleConfig by inject()
    val teamRepository: TeamRepository by inject()

    val teamPager = inlineKeyboardPager("downloadHomeworkFlow") {
        val teams = teamRepository.getByTrackerIdPaginated(user.id, offset, limit)
        val count = teamRepository.countByTrackerId(user.id)
        inlineKeyboard {
            teams.forEach {
                row {
                    dataButton(it.name, DownloadHomeworkQuery.OnTeamClicked(page, it.id))
                }
            }
            navigationRow(count)
        }
    }

    anyState {
        onDataCallbackQuery(DownloadHomeworkQuery.BackToTeams::class) { (data, query) ->
            runCatching {
                editMessageText(
                    query.messageCallbackQueryOrThrow().message.withContentOrThrow(),
                    ChooseTeam,
                    replyMarkup = with(teamPager) { page(data.page) }
                )
            }
            answer(query)
        }
        onDataCallbackQuery(DownloadHomeworkQuery.OnTeamClicked::class) { (data, query) ->
            runCatching {
                val team = teamRepository.get(data.teamId)
                editMessageText(
                    query.messageCallbackQueryOrThrow().message.withContentOrThrow(),
                    chooseModuleNumber(team.name),
                    replyMarkup = inlineKeyboard {
                        team.availableModules.chunked(2).forEach {
                            row {
                                dataButton(MenuStrings.Back, DownloadHomeworkQuery.BackToTeams(data.page))
                            }
                            row {
                                it.forEach {
                                    dataButton(
                                        (it.number.value).toString(),
                                        DownloadHomeworkQuery.OnModuleClicked(data.page, data.teamId, it.number)
                                    )
                                }
                            }
                        }
                    }
                )
            }
            answer(query)
        }
        onDataCallbackQuery(DownloadHomeworkQuery.OnModuleClicked::class) { (data, query) ->
            runCatching {
                val team = teamRepository.get(data.teamId)
                editMessageText(
                    query.messageCallbackQueryOrThrow().message.withContentOrThrow(),
                    moduleHomeworks(team.name, data.moduleNumber),
                    replyMarkup = inlineKeyboard {
                        val module = moduleConfig.modules.getValue(data.moduleNumber)
                        row {
                            dataButton(MenuStrings.Back, DownloadHomeworkQuery.OnTeamClicked(data.page, data.teamId))
                        }
                        for (task in module.tasks) {
                            val homework = homeworkRepository.get(data.teamId, task.number)
                            if (homework != null) {
                                row {
                                    urlButton(task.templateName, homework.url)
                                }
                            } else {
                                if (task == module.tasks.first()) {
                                    answer(query, NoHomeworksDone, showAlert = true)
                                    return@onDataCallbackQuery
                                }
                            }
                        }
                    }
                )
            }
            answer(query)
        }
    }

    state<DownloadHomeworkState> {
        onTransition {
            sendTextMessage(it, ChooseTeam, replyMarkup = with(teamPager) { firstPage })
            setStateQuiet(MenuState.Tracker.Teams)
        }
    }
}
