package ru.spbstu.preaccelerator.telegram.flows

import com.ithersta.tgbotapi.fsm.entities.triggers.onCommand
import com.ithersta.tgbotapi.fsm.entities.triggers.onDocument
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.files.downloadFile
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.requests.send.SendTextMessage
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.koin.core.context.GlobalContext
import ru.spbstu.preaccelerator.domain.entities.user.Curator
import ru.spbstu.preaccelerator.domain.usecases.AddMembersUseCase
import ru.spbstu.preaccelerator.domain.usecases.actions.AddTrackerAndTeamUseCase
import ru.spbstu.preaccelerator.domain.usecases.actions.MemberActions
import ru.spbstu.preaccelerator.telegram.StateMachineBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState
import ru.spbstu.preaccelerator.telegram.entities.state.LoadListOfUsersState
import ru.spbstu.preaccelerator.telegram.parsers.Xlsx
import ru.spbstu.preaccelerator.telegram.resources.strings.HelpStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings

private val addTrackerAndTeamUseCase: AddTrackerAndTeamUseCase by GlobalContext.get().inject()
private val addMembersUseCase: AddMembersUseCase by GlobalContext.get().inject()

fun StateMachineBuilder.LoadMembersAndTrackers() {
    role<Curator> {
        state<EmptyState> {
            onCommand("load", HelpStrings.Load) {
                setState(LoadListOfUsersState.WaitingForLoadListOfUsers)
            }
        }
        state<LoadListOfUsersState.WaitingForLoadListOfUsers> {
            onTransition {
                sendTextMessage(
                    it,
                    MessageStrings.LoadListOfUsers.LoadList
                )
            }
            onDocument { message ->
                val file = downloadFile(message.content.media)
                val teams = Xlsx.parseXlsxTrackers(file.inputStream())
                val members = Xlsx.parseXlsxMembers(file.inputStream())
                when (teams) {
                    is Xlsx.Result.OK -> {
                        when (members) {
                            is Xlsx.Result.OK -> {
                                addTrackerAndTeamUseCase(teams.value)
                                val resMembers = addMembersUseCase(members.value)
                                if (resMembers != null) {
                                    sendTextMessage(message.chat.id, MessageStrings.LoadListOfUsers.NotFindTeam
                                            + resMembers.reduce { acc, s -> "$acc, $s" })
                                } else {
                                    sendTextMessage(
                                        message.chat.id, MessageStrings.LoadListOfUsers.OkAddTeams
                                                + teams.value.size + ";\n"
                                                + MessageStrings.LoadListOfUsers.OkAddMembers
                                                + members.value.size
                                    )
                                }
                            }

                            is Xlsx.Result.BadFormat -> {
                                val ans =
                                    MessageStrings.LoadListOfUsers.BadFormatMembers + if (members.errorRows.size <= 5) {
                                        members.errorRows.map { it.toString() }.reduce { f, s -> "$f, $s" }
                                    } else {
                                        members.errorRows.subList(0, 5).map { it.toString() }
                                            .reduce { f, s -> "$f, $s" } + " и так далее."
                                    }
                                sendTextMessage(message.chat.id, ans)
                            }

                            else -> {}
                        }
                    }

                    is Xlsx.Result.InvalidFile -> sendTextMessage(
                        message.chat.id,
                        MessageStrings.LoadListOfUsers.InvalidFile
                    )

                    is Xlsx.Result.BadFormat -> {
                        when (members) {
                            is Xlsx.Result.BadFormat -> {
                                val ans =
                                    MessageStrings.LoadListOfUsers.BadFormatMembers + if (members.errorRows.size <= 5) {
                                        members.errorRows.map { it.toString() }.reduce { f, s -> "$f, $s" }
                                    } else {
                                        members.errorRows.subList(0, 5).map { it.toString() }
                                            .reduce { f, s -> "$f, $s" } + " и так далее. \n"
                                    } + MessageStrings.LoadListOfUsers.BadFormatTrackers + if (teams.errorRows.size <= 5) {
                                        teams.errorRows.map { it.toString() }.reduce { f, s -> "$f, $s" }
                                    } else {
                                        teams.errorRows.subList(0, 5).map { it.toString() }
                                            .reduce { f, s -> "$f, $s" } + " и так далее."
                                    }
                                sendTextMessage(message.chat.id, ans)
                            }

                            else -> {
                                val ans =
                                    MessageStrings.LoadListOfUsers.BadFormatTrackers + if (teams.errorRows.size <= 5) {
                                        teams.errorRows.map { it.toString() }.reduce { f, s -> "$f, $s" }
                                    } else {
                                        teams.errorRows.subList(0, 5).map { it.toString() }
                                            .reduce { f, s -> "$f, $s" } + " и так далее."
                                    }
                                sendTextMessage(message.chat.id, ans)
                            }
                        }
                    }
                }
                setState(EmptyState)
            }
        }
    }
}