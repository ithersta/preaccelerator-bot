package ru.spbstu.preaccelerator.telegram.flows.tracker

import com.ithersta.tgbotapi.fsm.entities.triggers.onDataCallbackQuery
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.*
import dev.inmo.tgbotapi.types.message.MarkdownV2
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module
import ru.spbstu.preaccelerator.domain.entities.module.ModuleConfig
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.domain.repository.HomeworkRepository
import ru.spbstu.preaccelerator.domain.repository.TeamRepository
import ru.spbstu.preaccelerator.telegram.RoleFilterBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.*
import ru.spbstu.preaccelerator.telegram.extensions.TeamExt.availableModules
import ru.spbstu.preaccelerator.telegram.extensions.TrackerExt.teams
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings.Homework.ModuleSpecific.AllMod
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings.Homework.ModuleSpecific.LastOnly
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.DownloadHomework.ChooseModuleNumber
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.DownloadHomework.ChooseTeam
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.DownloadHomework.DownloadOption
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.DownloadHomework.NoHomeworksDone
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.DownloadHomework.moduleHomeworks

fun RoleFilterBuilder<Tracker>.downloadHomeworkFlow() {
    val homeworkRepository: HomeworkRepository by inject()
    val moduleConfig: ModuleConfig by inject()
    val teamRep: TeamRepository by inject()
    state<DownloadHomeworkState> {
        onTransition {
            sendTextMessage(
                it,
                ChooseTeam,
                parseMode = MarkdownV2,
                replyMarkup = inlineKeyboard {
                    user.teams.chunked(2).forEach {
                        row {
                            it.forEach { dataButton(it.name, "team ${it.id.value}") }
                        }
                    }
                }
            )
        }
        onDataCallbackQuery(Regex("team \\d+")) {
            val teamId = Team.Id(it.data.split(" ").last().toLong())
            setState(WaitingForModuleSpecification(teamId))
        }
    }
    state<WaitingForModuleSpecification> {
        onTransition {
            sendTextMessage(
                it,
                DownloadOption,
                parseMode = MarkdownV2,
                replyMarkup = replyKeyboard(
                    resizeKeyboard = true,
                    oneTimeKeyboard = true
                ) {

                    row {
                        simpleButton(LastOnly)
                        simpleButton(AllMod)
                    }
                }
            )
        }
        onText {
            when (it.content.text) {
                LastOnly -> {
                    val lastModuleNumb = teamRep.get(state.team).availableModules.last().number
                    setState(ModuleHomework(state.team, lastModuleNumb))
                }

                AllMod -> {
                    setState(AllModules(state.team))
                }

                else -> {
                    sendTextMessage(
                        it.chat,
                        MessageStrings.DownloadHomework.Err
                    )
                }
            }
        }
    }
    state<ModuleHomework> {
        onTransition {
            sendTextMessage(
                it,
                moduleHomeworks(state.moduleNumber),
                parseMode = MarkdownV2,
                replyMarkup = inlineKeyboard {
                    val currentModule = moduleConfig.modules.getValue(state.moduleNumber)
                    for (task in currentModule.tasks) {
                        val homework = homeworkRepository.get(state.team, task.number)
                        if (homework != null) {
                            row {
                                urlButton(
                                    task.templateName,
                                    homework.url
                                )
                            }
                        } else {
                            if (task == currentModule.tasks.first()) {
                                sendTextMessage(
                                    it,
                                    NoHomeworksDone
                                )
                                break
                            }
                        }
                    }
                }
            )
            setState(EmptyState)
        }
    }
    state<AllModules> {
        onTransition {
            sendTextMessage(
                it,
                ChooseModuleNumber,
                replyMarkup = inlineKeyboard {
                    teamRep.get(state.team).availableModules.chunked(2).forEach {
                        row {
                            it.forEach { dataButton((it.number.value).toString(), "module ${it.number.value}") }
                        }
                    }
                }
            )
        }
        onDataCallbackQuery(Regex("module \\d+")) {
            val moduleNumber = Module.Number(it.data.split(" ").last().toInt())
            setState(ModuleHomework(state.team, moduleNumber))
        }
    }
}
