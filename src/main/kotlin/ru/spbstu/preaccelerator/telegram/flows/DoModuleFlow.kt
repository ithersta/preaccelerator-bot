package ru.spbstu.preaccelerator.telegram.flows

import com.ithersta.tgbotapi.fsm.entities.triggers.onDataCallbackQuery
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.*
import dev.inmo.tgbotapi.types.message.MarkdownV2
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.module.*
import ru.spbstu.preaccelerator.domain.entities.user.Member
import ru.spbstu.preaccelerator.telegram.StateMachineBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.*
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.AddInfoWord
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.NameOfLectureWord
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.NextPart
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.ShowPresentation
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.SpeakerWord
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.TaskNumber
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.WatchLecture


fun StateMachineBuilder.doModuleFlow() {

    val moduleConfig: ModuleConfig by inject()

    role<Member> {
        state<StartModule> {
            onTransition {
                val firstModule = moduleConfig.modules[0] ////потом вместо этого выбор модуля из доступных кнопок
                val startModule = ModuleState(firstModule.number, 0)
                setState(startModule)
            }
        }
        state<ModuleState> {
            onTransition {
                val currModule = moduleConfig.modules[state.moduleNumber.value]
                val part = currModule.parts[state.partIndex]
                when (part) {
                    is Lecture -> {
                        val text = NameOfLectureWord + part.name + "\n" + SpeakerWord + part.speaker
                        sendTextMessage(
                            it,
                            text,
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
                                row{
                                    dataButton(
                                        NextPart,
                                        "next"
                                    )
                                }
                            }
                        )
                    }

                    is AdditionalInfo -> sendTextMessage(
                        it,
                        AddInfoWord + part.text,
                        parseMode = MarkdownV2,
                        replyMarkup = inlineKeyboard {
                            row {
                                dataButton(
                                    NextPart,
                                    "next"
                                )
                            }
                        }
                    )

                    is Task -> {
                        sendTextMessage(
                            it,
                            TaskNumber + (part.number.value + 1) + "\n\n" + part.description,
                            parseMode = MarkdownV2,///после этого ждать документ с заполненным шаблоном и отправлять дз треккеру
                            replyMarkup = inlineKeyboard {
                                row {
                                    dataButton(
                                        NextPart,
                                        "next"
                                    )
                                }
                            }
                        )
                    }
                }
                onDataCallbackQuery(Regex("next")) {
                    var newState = ModuleState(state.moduleNumber, state.partIndex + 1)
                    val maxPart = currModule.parts.lastIndex
                    val maxModule = moduleConfig.modules.size
                    if (state.partIndex == maxPart) {
                        newState = ModuleState(moduleConfig.modules[state.moduleNumber.value + 1].number, 0)
                    }
                    if (state.moduleNumber.value + 1 == maxModule) {
                        setState(StartModule)
                    }
                    setState(newState)
                }
            }
        }
    }
}
