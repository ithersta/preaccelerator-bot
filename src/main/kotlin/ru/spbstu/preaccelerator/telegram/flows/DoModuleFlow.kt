package ru.spbstu.preaccelerator.telegram.flows

import com.ithersta.tgbotapi.fsm.entities.triggers.onDataCallbackQuery
import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.answers.answer
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.*
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import dev.inmo.tgbotapi.types.buttons.SimpleKeyboardButton
import dev.inmo.tgbotapi.types.message.MarkdownV2
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.module.*
import ru.spbstu.preaccelerator.domain.entities.user.Member
import ru.spbstu.preaccelerator.telegram.StateMachineBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.ModuleState
import ru.spbstu.preaccelerator.telegram.entities.state.StartModule
import ru.spbstu.preaccelerator.telegram.entities.state.WaitingForHomework
import ru.spbstu.preaccelerator.telegram.extensions.MemberExt.team
import ru.spbstu.preaccelerator.telegram.extensions.TeamExt.addHomework
import ru.spbstu.preaccelerator.telegram.extensions.TeamExt.availableModules
import ru.spbstu.preaccelerator.telegram.extensions.TeamExt.getHomework
import ru.spbstu.preaccelerator.telegram.flows.ModuleStateExt.module
import ru.spbstu.preaccelerator.telegram.flows.ModuleStateExt.part
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.SendHomework
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.additionalInfoMessage
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.doneTaskMessage
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.goodbyeModule
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.lectureMessage
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.nextModule
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.taskMessage
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.welcomeModule
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings.Module.DoTest
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings.Module.NextPart
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings.Module.ShowPresentation
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings.Module.WatchLecture
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings
import java.net.URL

fun StateMachineBuilder.doModuleFlow() {
    val moduleConfig: ModuleConfig by inject()
    role<Member> {
        var number = 0
        state<StartModule> {
            onTransition {
                sendTextMessage(
                    it, MenuStrings.Member.ChooseStep,
                    parseMode = MarkdownV2,
                    replyMarkup = replyKeyboard(
                        resizeKeyboard=true,
                        oneTimeKeyboard = true
                    )
                    {
                        val countOfModel = user.team.availableModules.size
                        when (countOfModel) {
                            1 -> {
                                row {
                                    simpleButton(moduleConfig.modules[0].name)
                                }
                            }

                            2, 4, 6, 8 -> {
                                for (i in 0 until countOfModel step 2) {
                                    row {
                                        simpleButton(moduleConfig.modules[i].name)
                                        simpleButton(moduleConfig.modules[i + 1].name)
                                    }
                                }

                            }

                            3, 5, 7 -> {
                                for (i in 0 until countOfModel-1 step 2) {
                                    row {
                                        simpleButton(moduleConfig.modules[i].name)
                                        simpleButton(moduleConfig.modules[i + 1].name)
                                    }
                                }
                                row { simpleButton(user.team.availableModules[countOfModel-1].name)  }
                            }
                        }
                    }
                )
            }
            onText { message ->
                val model = message.content.text
                for (i in 0..7) {
                    if (moduleConfig.modules[i].name == model) {
                        val firstModule = moduleConfig.modules[i]
                        val startModule = ModuleState(firstModule.number, 0)
                        setState(startModule)
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
            onDataCallbackQuery(Regex("part \\d+")) {
                answer(it)
                val partIndex = it.data.split(' ').last().toInt()
                if (partIndex <= state.partIndex) {
                    return@onDataCallbackQuery
                }
                val newState = ModuleState(state.moduleNumber, partIndex)
                val maxPart = moduleConfig.modules[state.moduleNumber.value].parts.lastIndex
                val maxModule = moduleConfig.modules.lastIndex
                if (state.partIndex == maxPart) {
                    sendTextMessage(
                        it.from,
                        goodbyeModule(moduleConfig, state.moduleNumber),
                        parseMode = MarkdownV2,
                        replyMarkup = inlineKeyboard {
                            row {
                                urlButton(
                                    DoTest,
                                    moduleConfig.modules[state.moduleNumber.value].finalTestUrl
                                )
                                if (state.moduleNumber.value != maxModule) {
                                    dataButton(
                                        nextModule(state.moduleNumber),
                                        "module ${state.moduleNumber.value + 1}"
                                    )
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
                setState(WaitingForHomework(state, taskNumber))
            }
        }
        state<WaitingForHomework> {
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
                }
                setState(state.returnTo)
            }
        }
    }
}

private object ModuleStateExt : KoinComponent {
    private val moduleConfig: ModuleConfig by inject()

    val ModuleState.module get() = moduleConfig.modules[moduleNumber.value]
    val ModuleState.part get() = module.parts[partIndex]
}