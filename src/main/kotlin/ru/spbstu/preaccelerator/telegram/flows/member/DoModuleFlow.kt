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
import ru.spbstu.preaccelerator.telegram.StateMachineBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.*
import ru.spbstu.preaccelerator.telegram.extensions.MemberExt.team
import ru.spbstu.preaccelerator.telegram.extensions.TeamExt.addHomework
import ru.spbstu.preaccelerator.telegram.extensions.TeamExt.availableModules
import ru.spbstu.preaccelerator.telegram.extensions.TeamExt.getHomework
import ru.spbstu.preaccelerator.telegram.flows.member.ModuleStateExt.module
import ru.spbstu.preaccelerator.telegram.flows.member.ModuleStateExt.part
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
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings
import java.net.URL

fun StateMachineBuilder.doModuleFlow() {
    val moduleConfig: ModuleConfig by inject()

    role<Member> {
        state<EmptyState> {
            onTransition {
                sendTextMessage(
                    it,
                    ButtonStrings.ChooseModule.Button,
                    parseMode = MarkdownV2
                )
                val number = 0
                sendTextMessage(
                    it,
                    MessageStrings.ChooseModuleAction.Message,
                    parseMode = MarkdownV2 ///здесь меню выбора номера модуля
                )
                ///присваиваем number значение в соответствии с выбранным модулем
                setState(ChooseModuleAction(moduleConfig.modules[number].number))
            }
        }
        state<ChooseModuleAction> {
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
                val moduleNumb = state.moduleNumber
                val moduleNumbInt = moduleNumb.value
                val mess = message.content.text
                when (mess) {
                    ButtonStrings.ChooseModule.DoEntireModule -> {
                        setState(ModuleState(moduleNumb, 0))
                    }

                    ButtonStrings.ChooseModule.WatchLectures -> {
                        sendTextMessage(
                            message.chat,
                            MessageStrings.ChooseModuleAction.ModuleLectures,
                            parseMode = MarkdownV2,
                            replyMarkup = inlineKeyboard {
                                moduleConfig.modules[moduleNumbInt].parts.forEach { part ->
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
                                        moduleConfig.modules[state.moduleNumber.value].finalTestUrl
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
