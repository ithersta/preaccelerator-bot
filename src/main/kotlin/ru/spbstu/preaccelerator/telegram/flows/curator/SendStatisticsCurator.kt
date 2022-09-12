package ru.spbstu.preaccelerator.telegram.flows.curator

import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.send.media.sendDocument
import dev.inmo.tgbotapi.requests.abstracts.asMultipartFile
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.user.Curator
import ru.spbstu.preaccelerator.domain.repository.HomeworkRepository
import ru.spbstu.preaccelerator.domain.repository.TeamRepository
import ru.spbstu.preaccelerator.telegram.RoleFilterBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState
import ru.spbstu.preaccelerator.telegram.entities.state.SendStatisticsTeamsState
import ru.spbstu.preaccelerator.telegram.parsers.Xlsx
import ru.spbstu.preaccelerator.telegram.resources.strings.SpreadsheetStrings

fun RoleFilterBuilder<Curator>.sendStatisticsCurator() {
    state<SendStatisticsTeamsState>{
        val teamRepository: TeamRepository by inject()
        val homeworkRepository: HomeworkRepository by inject()
        onTransition { messager ->
            val spreadsheet = Xlsx.createStatisticsSpreadsheet(teamRepository.getAll(), homeworkRepository.getAll())
            sendDocument(messager, spreadsheet.asMultipartFile("${SpreadsheetStrings.StatisticsTable.FileName}.xlsx"))
            setState(EmptyState)
        }
    }
}