package ru.spbstu.preaccelerator.telegram.flows.tracker

import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.send.media.sendDocument
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.requests.abstracts.asMultipartFile
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.user.Curator
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.domain.repository.HomeworkRepository
import ru.spbstu.preaccelerator.domain.repository.TeamRepository
import ru.spbstu.preaccelerator.domain.repository.TrackerRepository
import ru.spbstu.preaccelerator.domain.repository.UserPhoneNumberRepository
import ru.spbstu.preaccelerator.telegram.RoleFilterBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState
import ru.spbstu.preaccelerator.telegram.entities.state.SendStatisticsTeamsState
import ru.spbstu.preaccelerator.telegram.extensions.TrackerExt.teams
import ru.spbstu.preaccelerator.telegram.parsers.Xlsx
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.SpreadsheetStrings

fun RoleFilterBuilder<Tracker>.sendStatisticsTracker() {
    state<SendStatisticsTeamsState> {
        val homeworkRepository: HomeworkRepository by inject()
        onTransition { messager ->
            try {
                val spreadsheet = Xlsx.createStatisticsSpreadsheet(
                    user.teams,
                    homeworkRepository.getAll()
                )
                sendDocument(
                    messager,
                    spreadsheet.asMultipartFile("${SpreadsheetStrings.StatisticsTable.FileName}.xlsx")
                )
            } catch (error: RuntimeException) {
                sendTextMessage(messager,MessageStrings.SendStatistics.NoTeams)
            }
            finally {
                setState(EmptyState)
            }

        }
    }
}