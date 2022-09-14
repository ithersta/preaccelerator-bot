package ru.spbstu.preaccelerator.telegram.flows.curator

import com.ithersta.tgbotapi.fsm.StatefulContext
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.send.media.sendDocument
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.requests.abstracts.asMultipartFile
import org.koin.core.component.inject
import org.koin.core.context.GlobalContext
import ru.spbstu.preaccelerator.domain.entities.user.Curator
import ru.spbstu.preaccelerator.domain.entities.user.PreacceleratorUser
import ru.spbstu.preaccelerator.domain.repository.HomeworkRepository
import ru.spbstu.preaccelerator.domain.repository.TeamRepository
import ru.spbstu.preaccelerator.telegram.RoleFilterBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.DialogState
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState
import ru.spbstu.preaccelerator.telegram.entities.state.SendStatisticsTeamsState
import ru.spbstu.preaccelerator.telegram.parsers.Xlsx
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.SpreadsheetStrings

val teamRepository: TeamRepository by GlobalContext.get().inject()
val homeworkRepository: HomeworkRepository by GlobalContext.get().inject()
suspend fun StatefulContext<DialogState, PreacceleratorUser, *, Curator>.sendStatisticsCurator() {
    try {
        val spreadsheet = Xlsx.createStatisticsSpreadsheet(teamRepository.getAll(), homeworkRepository.getAll())
        sendDocument(
            user.userId,
            spreadsheet.asMultipartFile("${SpreadsheetStrings.StatisticsTable.FileName}.xlsx")
        )
    } catch (error: RuntimeException) {
        sendTextMessage(user.userId, MessageStrings.SendStatistics.NoTeams)
        error.printStackTrace()
    }
}