package ru.spbstu.preaccelerator.telegram.flows.curator

import com.ithersta.tgbotapi.fsm.entities.triggers.onDocument
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.files.downloadFile
import dev.inmo.tgbotapi.extensions.api.send.media.sendDocument
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.requests.abstracts.InputFile
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import io.ktor.utils.io.streams.*
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.user.Curator
import ru.spbstu.preaccelerator.domain.usecases.AddUsersUseCase
import ru.spbstu.preaccelerator.telegram.RoleFilterBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.AddUsersState
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState
import ru.spbstu.preaccelerator.telegram.parsers.Xlsx
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.AddUsers.TemplateFilename

fun RoleFilterBuilder<Curator>.addUsersFlow() {
    val addUsers: AddUsersUseCase by inject()
    state<AddUsersState.WaitingForDocument> {
        onTransition {
            val document = InputFile.fromInput(
                "$TemplateFilename.xlsx"
            ) { this::class.java.getResourceAsStream("/users.xlsx")!!.asInput() }
            sendDocument(
                chatId = it,
                text = MessageStrings.AddUsers.WaitDocument,
                document = document,
                replyMarkup = ReplyKeyboardRemove()
            )
        }
        onDocument { message ->
            downloadFile(message.content.media).inputStream().use { inputStream ->
                when (val users = Xlsx.parseUsers(inputStream)) {
                    is Xlsx.Result.OK -> {
                        val result = addUsers(users.value.members, users.value.teams)
                        sendTextMessage(message.chat, MessageStrings.AddUsers.result(result))
                        setState(EmptyState)
                    }

                    is Xlsx.Result.BadFormat -> sendTextMessage(
                        message.chat,
                        MessageStrings.AddUsers.badFormat(users.errors)
                    )

                    is Xlsx.Result.InvalidFile -> sendTextMessage(
                        message.chat,
                        MessageStrings.AddUsers.InvalidFile
                    )
                }
            }
        }
    }
}
