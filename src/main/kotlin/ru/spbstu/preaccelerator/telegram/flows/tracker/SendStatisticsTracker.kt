package ru.spbstu.preaccelerator.telegram.flows.tracker

import com.ithersta.tgbotapi.fsm.StatefulContext
import dev.inmo.tgbotapi.extensions.api.send.media.sendDocument
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.requests.abstracts.asMultipartFile
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.content.TextContent
import org.koin.core.context.GlobalContext
import ru.spbstu.preaccelerator.domain.entities.user.PreacceleratorUser
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.domain.repository.HomeworkRepository
import ru.spbstu.preaccelerator.telegram.entities.state.DialogState
import ru.spbstu.preaccelerator.telegram.extensions.TrackerExt.teams
import ru.spbstu.preaccelerator.telegram.parsers.Xlsx
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.SpreadsheetStrings

val homeworkRepository: HomeworkRepository by GlobalContext.get().inject()

suspend fun StatefulContext<DialogState, PreacceleratorUser, *, Tracker>.sendStatisticsTracker(message: CommonMessage<TextContent>) {
    try {
        val spreadsheet = Xlsx.createStatisticsSpreadsheet(
            user.teams,
            homeworkRepository.getAll()
        )
        sendDocument(
            message.chat,
            spreadsheet.asMultipartFile("${SpreadsheetStrings.StatisticsTable.FileName}.xlsx")
        )
    } catch (error: RuntimeException) {
        sendTextMessage(message.chat, MessageStrings.SendStatistics.NoTeams)
    }
}