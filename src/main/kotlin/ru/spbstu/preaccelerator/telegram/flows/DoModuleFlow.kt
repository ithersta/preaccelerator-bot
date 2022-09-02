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
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.AdditionalInfoString
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.DoTest
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.GetFinalTestUrl
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.GoodByeModule
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.LectureString
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.NameOfLectureWord
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.NextModule
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.NextPart
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.ShowPresentation
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.SpeakerWord
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.TaskString
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.WatchLecture
import ru.spbstu.preaccelerator.telegram.resources.modules.ModuleStrings.WelcomeModule


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
                val currModule = moduleConfig.modules[state.moduleNumber.value]
                if (state.partIndex == 0) {
                    sendTextMessage(
                        it,
                        WelcomeModule(currModule),
                        parseMode = MarkdownV2
                    )
                }
                val part = currModule.parts[state.partIndex]
                when (part) {
                    is Lecture -> {
                        sendTextMessage(
                            it,
                            LectureString(part),
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
                                        "nextPart"
                                    )
                                }
                            }
                        )
                    }

                    is AdditionalInfo -> sendTextMessage(
                        it,
                        AdditionalInfoString(part),
                        parseMode = MarkdownV2,
                        replyMarkup = inlineKeyboard {
                            row {
                                dataButton(
                                    NextPart,
                                    "nextPart"
                                )
                            }
                        }
                    )

                    is Task -> {
                        sendTextMessage(
                            it,
                            TaskString(part),
                            parseMode = MarkdownV2,///после этого ждать документ с заполненным шаблоном и отправлять дз треккеру
                            replyMarkup = inlineKeyboard {
                                row {
                                    dataButton(
                                        NextPart,
                                        "nextPart"
                                    )
                                }
                            }
                        )
                    }
                }
            }
            onDataCallbackQuery(Regex("nextPart")) {
                val newState = ModuleState(state.moduleNumber, state.partIndex + 1)
                val maxPart = moduleConfig.modules[state.moduleNumber.value].parts.lastIndex
                val maxModule = moduleConfig.modules.lastIndex
                if (state.partIndex == maxPart) {
                    sendTextMessage(
                        it.from,
                        GoodByeModule(state.moduleNumber),
                        parseMode = MarkdownV2,
                        replyMarkup = inlineKeyboard {
                            row {
                                urlButton(
                                    DoTest,
                                    GetFinalTestUrl(state.moduleNumber)!!
                                )
                                if (state.moduleNumber.value != maxModule){
                                dataButton(
                                    NextModule(state.moduleNumber),
                                    "nextModule"
                                )}
                            }
                        }
                    )
                }
                else {
                    setState(newState)
                }
            }
            onDataCallbackQuery(Regex("nextModule")){
                val maxModule = moduleConfig.modules.lastIndex
                if (state.moduleNumber.value == maxModule) {
                    setState(StartModule) //// изменить на состояние выбора номера модуля
                }
                setState(ModuleState(moduleConfig.modules[state.moduleNumber.value + 1].number, 0))
            }
        }
    }
}
