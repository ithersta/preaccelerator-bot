package ru.spbstu.preaccelerator.telegram.flows

import com.ithersta.tgbotapi.fsm.entities.triggers.onDataCallbackQuery
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.answers.answer
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.dataButton
import dev.inmo.tgbotapi.extensions.utils.types.buttons.inlineKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.row
import dev.inmo.tgbotapi.extensions.utils.types.buttons.urlButton
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import dev.inmo.tgbotapi.types.message.MarkdownV2
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.module.*
import ru.spbstu.preaccelerator.domain.entities.user.Member
import ru.spbstu.preaccelerator.telegram.StateMachineBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.ModuleState
import ru.spbstu.preaccelerator.telegram.entities.state.StartModule
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.additionalInfoMessage
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.goodbyeModule
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.lectureMessage
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.nextModule
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.taskMessage
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.welcomeModule
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings.Module.DoTest
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings.Module.NextPart
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings.Module.ShowPresentation
import ru.spbstu.preaccelerator.telegram.resources.strings.ButtonStrings.Module.WatchLecture


fun StateMachineBuilder.doModuleFlow() {
    val moduleConfig: ModuleConfig by inject()

    role<Member> {
        state<StartModule> {
            onTransition {
                val moduleNumb = 0////потом вместо нуля выбор номера из доступных кнопок
                val firstModule = moduleConfig.modules[moduleNumb]
                val startModule = ModuleState(firstModule.number, 0)
                setState(startModule)
            }
        }
        state<ModuleState> {
            onTransition {
                val currentModule = moduleConfig.modules[state.moduleNumber.value]
                if (state.partIndex == 0) {
                    sendTextMessage(
                        it,
                        welcomeModule(currentModule),
                        parseMode = MarkdownV2,
                        replyMarkup = ReplyKeyboardRemove()
                    )
                }
                when (val part = currentModule.parts[state.partIndex]) {
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
                        sendTextMessage(
                            it,
                            taskMessage(part),
                            parseMode = MarkdownV2,///после этого ждать документ с заполненным шаблоном и отправлять дз треккеру
                            replyMarkup = inlineKeyboard {
                                row {
                                    dataButton(
                                        NextPart,
                                        "part ${state.partIndex + 1}"
                                    )
                                }
                            }
                        )
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
        }
    }
}
