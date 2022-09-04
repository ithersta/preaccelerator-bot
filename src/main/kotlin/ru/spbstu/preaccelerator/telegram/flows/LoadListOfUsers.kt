package ru.spbstu.preaccelerator.telegram.flows

import com.ithersta.tgbotapi.fsm.entities.triggers.onDocument
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.files.downloadFile
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.user.Curator
import ru.spbstu.preaccelerator.domain.usecases.AddTrackerTeamAndMemberUseCase
import ru.spbstu.preaccelerator.telegram.StateMachineBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState
import ru.spbstu.preaccelerator.telegram.entities.state.LoadListOfUsersState
import ru.spbstu.preaccelerator.telegram.parsers.Xlsx
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings

fun StateMachineBuilder.loadMembersAndTrackers() {
    val addTrackerTeamAndMemberUseCase: AddTrackerTeamAndMemberUseCase by inject()
    role<Curator> {
        state<LoadListOfUsersState.WaitingForDocument> {
            onTransition {
                sendTextMessage(
                    it,
                    MessageStrings.LoadListOfUsers.WaitDocument
                )
            }
            onDocument { message ->
                downloadFile(message.content.media).inputStream().use { file ->
                    val teams = Xlsx.parseXlsxTeams(file, 1)
                    file.reset()
                    val members = Xlsx.parseXlsxTeams(file, 0)
                    when (teams) {
                        is Xlsx.Result.OK -> {
                            when (members) {
                                is Xlsx.Result.OK -> {
                                    val notFoundTeams = addTrackerTeamAndMemberUseCase(teams.value, members.value)
                                    if (notFoundTeams.isNotEmpty()) {
                                        sendTextMessage(
                                            message.chat.id,
                                            MessageStrings.LoadListOfUsers.notFoundTeams(notFoundTeams.toList())
                                        )
                                    } else {
                                        sendTextMessage(
                                            message.chat.id,
                                            MessageStrings.LoadListOfUsers.success(
                                                membersCount = members.value.size,
                                                teamsCount = teams.value.size
                                            )
                                        )
                                    }
                                }

                                is Xlsx.Result.BadFormat -> {
                                    sendTextMessage(
                                        message.chat.id,
                                        MessageStrings.LoadListOfUsers.badFormat(members.errorRows, null)
                                    )
                                }

                                is Xlsx.Result.InvalidFile -> sendTextMessage(
                                    message.chat.id,
                                    MessageStrings.LoadListOfUsers.InvalidFile
                                )
                            }
                        }

                        is Xlsx.Result.InvalidFile -> sendTextMessage(
                            message.chat.id,
                            MessageStrings.LoadListOfUsers.InvalidFile
                        )

                        is Xlsx.Result.BadFormat -> {
                            if (members is Xlsx.Result.BadFormat) {
                                sendTextMessage(
                                    message.chat.id,
                                    MessageStrings.LoadListOfUsers.badFormat(members.errorRows, teams.errorRows)
                                )
                            } else {
                                sendTextMessage(
                                    message.chat.id,
                                    MessageStrings.LoadListOfUsers.badFormat(null, teams.errorRows)
                                )
                            }
                        }
                    }
                    setState(EmptyState)
                }
            }
        }
    }
}
