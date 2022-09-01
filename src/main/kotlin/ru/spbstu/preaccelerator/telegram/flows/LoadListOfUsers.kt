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
import ru.spbstu.preaccelerator.domain.usecases.actions.AddTrackerTeamAndMemberUseCase
import ru.spbstu.preaccelerator.domain.usecases.actions.MemberActions
import ru.spbstu.preaccelerator.telegram.StateMachineBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState
import ru.spbstu.preaccelerator.telegram.entities.state.LoadListOfUsersState
import ru.spbstu.preaccelerator.telegram.parsers.Xlsx
import ru.spbstu.preaccelerator.telegram.resources.strings.HelpStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings

private val addTrackerTeamAndMemberUseCase: AddTrackerTeamAndMemberUseCase by GlobalContext.get().inject()

fun StateMachineBuilder.loadMembersAndTrackers() {
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
                val teams = Xlsx.parseXlsxTeams(file.inputStream())
                val members = Xlsx.parseXlsxMembers(file.inputStream())
                when (teams) {
                    is Xlsx.Result.OK -> {
                        when (members) {
                            is Xlsx.Result.OK -> {
                                val resMembers = addTrackerTeamAndMemberUseCase(teams.value, members.value)
                                if (resMembers.isNotEmpty()) {
                                    sendTextMessage(message.chat.id, MessageStrings.LoadListOfUsers.NotFindTeam
                                            + resMembers.joinToString { it })
                                } else {
                                    sendTextMessage(
                                        message.chat.id,
                                        "${MessageStrings.LoadListOfUsers.OkAddTeams} ${teams.value.size};\n"
                                                + "${MessageStrings.LoadListOfUsers.OkAddMembers}${members.value.size}"
                                    )
                                }
                            }

                            is Xlsx.Result.BadFormat -> {
                                sendTextMessage(
                                    message.chat.id,
                                    MessageStrings.LoadListOfUsers.badFormat(members.errorRows, null)
                                )
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
                                sendTextMessage(
                                    message.chat.id,
                                    MessageStrings.LoadListOfUsers.badFormat(members.errorRows, teams.errorRows)
                                )
                            }

                            else -> {
                                sendTextMessage(
                                    message.chat.id,
                                    MessageStrings.LoadListOfUsers.badFormat(null, teams.errorRows)
                                )
                            }
                        }
                    }
                }
                setState(EmptyState)
            }
        }
    }
}