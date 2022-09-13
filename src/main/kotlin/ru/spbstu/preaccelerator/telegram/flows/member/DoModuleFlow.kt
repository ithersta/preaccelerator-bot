package ru.spbstu.preaccelerator.telegram.flows.member

import com.ithersta.tgbotapi.fsm.entities.triggers.onDataCallbackQuery
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.answers.answer
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.*
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import dev.inmo.tgbotapi.types.message.MarkdownV2
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.module.*
import ru.spbstu.preaccelerator.domain.entities.user.Member
import ru.spbstu.preaccelerator.domain.repository.TrackerRepository
import ru.spbstu.preaccelerator.telegram.RoleFilterBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.*
import ru.spbstu.preaccelerator.telegram.extensions.MemberExt.team
import ru.spbstu.preaccelerator.telegram.extensions.TeamExt.addHomework
import ru.spbstu.preaccelerator.telegram.extensions.TeamExt.availableModules
import ru.spbstu.preaccelerator.telegram.extensions.TeamExt.getHomework
import ru.spbstu.preaccelerator.telegram.extensions.TrackerExt.userId
import ru.spbstu.preaccelerator.telegram.flows.member.ModuleStateExt.module
import ru.spbstu.preaccelerator.telegram.flows.member.ModuleStateExt.part
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.SendHomework
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.additionalInfoMessage
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.doneTaskMessage
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.goodbyeModule
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.lectureMessage
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.module
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.taskMessage
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.welcomeModule
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings.Homework.SeeHomework
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings.Module.DoTest
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings.Module.NextPart
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings.Module.ShowPresentation
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings.Module.WatchLecture
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.NotificationStrings.homeworkDownloaded
import java.net.URL

fun RoleFilterBuilder<Member>.doModuleFlow() {
    val moduleConfig: ModuleConfig by inject()
    val trackerRep: TrackerRepository by inject()

    state<ChooseModuleState> {
        onTransition {
            sendTextMessage(
                it,
                ButtonStrings.ChooseModule.Button,
                replyMarkup = replyKeyboard(
                    resizeKeyboard = true,
                    oneTimeKeyboard = true
                ) {
                    user.team.availableModules.chunked(2).forEach {
                        row {
                            it.forEach { simpleButton(it.name) }
                        }
                    }
                }
            )
        }
        onText { message ->
            val moduleName = message.content.text
            val module = moduleConfig.modules.values.find { it.name == moduleName } ?: run {
                sendTextMessage(
                    message.chat,
                    MessageStrings.ChooseModuleAction.Err,
                    parseMode = MarkdownV2
                )
                return@onText
            }
            setState(ChooseModuleActionState(module.number))
        }
    }
    state<ChooseModuleActionState> {
        onTransition {
            sendTextMessage(
                it,
                MessageStrings.ChooseModuleAction.ChooseModuleAction,
                parseMode = MarkdownV2,
                replyMarkup = replyKeyboard(
                    block = {
                        row {
                            simpleButton(ButtonStrings.ChooseModule.DoEntireModule)
                        }
                        row {
                            simpleButton(ButtonStrings.ChooseModule.WatchLectures)
                            simpleButton(ButtonStrings.ChooseModule.DoTest)
                        }
                    },
                    resizeKeyboard = true
                )
            )
        }

        onText { message ->
            when (message.content.text) {
                ButtonStrings.ChooseModule.DoEntireModule -> {
                    setState(ModuleState(state.moduleNumber, 0))
                }

                ButtonStrings.ChooseModule.WatchLectures -> {
                    sendTextMessage(
                        message.chat,
                        MessageStrings.ChooseModuleAction.ModuleLectures,
                        parseMode = MarkdownV2,
                        replyMarkup = inlineKeyboard {
                            moduleConfig.modules.getValue(state.moduleNumber).parts.forEach { part ->
                                if (part is Lecture) {
                                    row {
                                        urlButton(
                                            part.name,
                                            part.url
                                        )
                                    }
                                }
                            }
                        }
                    )
                    setState(EmptyState)
                }

                ButtonStrings.ChooseModule.DoTest -> {
                    sendTextMessage(
                        message.chat,
                        goodbyeModule(moduleConfig, state.moduleNumber),
                        parseMode = MarkdownV2,
                        replyMarkup = inlineKeyboard {
                            row {
                                urlButton(
                                    DoTest,
                                    moduleConfig.modules.getValue(state.moduleNumber).finalTestUrl
                                )
                            }
                        })
                    setState(EmptyState)
                }

                else -> {
                    sendTextMessage(
                        message.chat,
                        MessageStrings.ChooseModuleAction.Err,
                        parseMode = MarkdownV2
                    )
                }
            }
        }
    }


    state<ModuleState> {
        onTransition {
            require(state.module in user.team.availableModules)
            if (state.partIndex == 0) {
                sendTextMessage(
                    it,
                    welcomeModule(state.module),
                    parseMode = MarkdownV2,
                    replyMarkup = ReplyKeyboardRemove()
                )
            }
            when (val part = state.part) {
                is Lecture -> {
                    sendTextMessage(
                        it,
                        lectureMessage(part),
                        parseMode = MarkdownV2,
                        replyMarkup = inlineKeyboard {
                            row {
                                urlButton(
                                    WatchLecture,
                                    part.url
                                )
                                urlButton(
                                    ShowPresentation,
                                    part.presentationUrl
                                )
                            }
                            row {
                                dataButton(
                                    NextPart,
                                    "part ${state.partIndex + 1}"
                                )
                            }
                        }
                    )
                }

                is AdditionalInfo -> sendTextMessage(
                    it,
                    additionalInfoMessage(part),
                    parseMode = MarkdownV2,
                    replyMarkup = inlineKeyboard {
                        row {
                            dataButton(
                                NextPart,
                                "part ${state.partIndex + 1}"
                            )
                        }
                    }
                )

                is Task -> {
                    val homework = user.team.getHomework(part.number)
                    if (homework != null) {
                        sendTextMessage(
                            it,
                            doneTaskMessage(part, homework.url),
                            replyMarkup = inlineKeyboard {
                                row {
                                    dataButton(
                                        NextPart,
                                        "part ${state.partIndex + 1}"
                                    )
                                }
                            }
                        )
                    } else {
                        sendTextMessage(
                            it,
                            taskMessage(part),
                            parseMode = MarkdownV2,
                            replyMarkup = inlineKeyboard {
                                row {
                                    dataButton(
                                        ButtonStrings.Module.HomeworkAlreadyDone,
                                        "done"
                                    )
                                }
                                row {
                                    dataButton(
                                        ButtonStrings.Module.SendHomework,
                                        "send ${part.number.value}"
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
        onDataCallbackQuery(Regex("part \\d+")) { query ->
            answer(query)
            val partIndex = query.data.split(' ').last().toInt()
            if (partIndex <= state.partIndex) {
                return@onDataCallbackQuery
            }
            val newState = ModuleState(state.moduleNumber, partIndex)
            val maxPart = moduleConfig.modules.getValue(state.moduleNumber).parts.lastIndex
            val maxModule = moduleConfig.modules.maxOf { it.key.value }
            if (state.partIndex == maxPart) {
                sendTextMessage(
                    query.from,
                    goodbyeModule(moduleConfig, state.moduleNumber),
                    parseMode = MarkdownV2,
                    replyMarkup = inlineKeyboard {
                        row {
                            urlButton(
                                DoTest,
                                moduleConfig.modules.getValue(state.moduleNumber).finalTestUrl
                            )
                            val nextModuleNumber = Module.Number(state.moduleNumber.value + 1)
                                .takeIf { moduleConfig.modules.containsKey(it) }
                            if (nextModuleNumber != null) {
                                dataButton(
                                    module(nextModuleNumber),
                                    "module ${nextModuleNumber.value}"
                                )
                            } else {
                                setState(EmptyState)
                            }
                        }
                    }
                )
            } else {
                setState(newState)
            }
        }
        onDataCallbackQuery(Regex("module \\d+")) {
            answer(it)
            val moduleNumber = Module.Number(it.data.split(' ').last().toInt())
            if (moduleNumber.value <= state.moduleNumber.value) {
                return@onDataCallbackQuery
            }
            setState(ModuleState(moduleNumber, 0))
        }
        onDataCallbackQuery(Regex("done")) {
            val task = state.part as? Task ?: return@onDataCallbackQuery
            val homework = user.team.getHomework(task.number)
            if (homework != null) {
                setState(state)
                answer(it)
            } else {
                answer(it, showAlert = true, text = ModuleStrings.HomeworkNotSentAlert)
            }
        }
        onDataCallbackQuery(Regex("send \\d+")) {
            answer(it)
            val taskNumber = Task.Number(it.data.split(' ').last().toInt())
            setState(WaitingForHomeworkState(state, taskNumber))
        }
    }
    state<WaitingForHomeworkState> {
        onTransition {
            sendTextMessage(
                it,
                SendHomework,
                replyMarkup = flatInlineKeyboard {
                    dataButton(ButtonStrings.Cancel, "cancel")
                }
            )
        }
        onDataCallbackQuery(Regex("cancel")) {
            setState(state.returnTo)
        }
        onText { message ->
            val task = moduleConfig.tasks.first { it.number == state.taskNumber }
            val url = runCatching { URL(message.content.text) }.getOrElse {
                sendTextMessage(message.chat, ModuleStrings.Error.MalformedHomeworkUrl)
                return@onText
            }
            if (!user.team.addHomework(task.number, url.toString())) {
                sendTextMessage(
                    chat = message.chat,
                    text = ModuleStrings.Error.HomeworkWasAlreadyAdded
                )
            } else {
                runCatching {
                    sendTextMessage(
                        trackerRep.get(user.team.trackerId).userId!!,
                        homeworkDownloaded(task.number, user.team),
                        parseMode = MarkdownV2,
                        replyMarkup = inlineKeyboard {
                            row {
                                urlButton(
                                    SeeHomework,
                                    url.toString()
                                )
                            }
                        }
                    )
                }
            }
            setState(state.returnTo)
        }
    }
}


private object ModuleStateExt : KoinComponent {
    private val moduleConfig: ModuleConfig by inject()

    val ModuleState.module get() = moduleConfig.modules.getValue(moduleNumber)
    val ModuleState.part get() = module.parts[partIndex]
}
