package ru.spbstu.preaccelerator.telegram

import dev.inmo.tgbotapi.extensions.api.send.media.sendDocument
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.requests.abstracts.asMultipartFile
import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.types.toChatId
import io.ktor.server.engine.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.data.DatabaseCredentials
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.hours

private val logger = KotlinLogging.logger { }

@Single
class DatabaseDumper(private val credentials: DatabaseCredentials) {
    fun BehaviourContext.start() = launch {
        val chatId = System.getenv()["DUMP_USER_ID"]?.toLong()?.toChatId() ?: return@launch
        while (true) {
            dump(chatId)
            delay(1.hours)
        }
    }

    private suspend fun BehaviourContext.dump(chatId: ChatId) {
        runCatching {
            val dumpedAt = OffsetDateTime.now()
            val process = ProcessBuilder()
                .command(
                    buildList {
                        add("pg_dump")
                        add(credentials.name)
                        credentials.user?.let {
                            add("-U")
                            add(it)
                        }
                        credentials.hostname?.let {
                            add("-h")
                            add(it)
                        }
                        credentials.port?.let {
                            add("-p")
                            add(it.toString())
                        }
                    }
                ).also {
                    it.environment()["PGPASSWORD"] = credentials.password ?: ""
                }
                .start()
            val document = process.inputStream.readBytes()
                .asMultipartFile("dump ${dumpedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)}.sql")
            sendDocument(
                chatId = chatId,
                document = document,
                disableNotification = true
            )
        }.onFailure { e ->
            logger.info(e) { "Couldn't dump database" }
        }
    }
}
