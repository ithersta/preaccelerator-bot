package ru.spbstu.preaccelerator

import com.ithersta.tgbotapi.fsm.entities.StateMachine
import dev.inmo.tgbotapi.bot.ktor.telegramBot
import dev.inmo.tgbotapi.bot.settings.limiters.CommonLimiter
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import org.koin.core.context.startKoin
import ru.spbstu.preaccelerator.telegram.CreateInitialCuratorToken
import ru.spbstu.preaccelerator.telegram.DatabaseDumper
import ru.spbstu.preaccelerator.telegram.notifications.Notifiers
import ru.spbstu.preaccelerator.telegram.readToken

suspend fun main() {
    val application = startKoin { modules(preacceleratorModule) }
    val stateMachine: StateMachine<*, *, *> by application.koin.inject()
    val databaseDumper: DatabaseDumper by application.koin.inject()
    val createInitialCuratorToken: CreateInitialCuratorToken by application.koin.inject()
    val notifiers: Notifiers by application.koin.inject()
    telegramBot(readToken()) {
        requestsLimiter = CommonLimiter(lockCount = 30, regenTime = 1000)
        client = HttpClient(OkHttp)
    }.buildBehaviourWithLongPolling {
        stateMachine.run { collect() }
        databaseDumper.run { start() }
        createInitialCuratorToken.run { invoke() }
        notifiers.run { start() }
    }.join()
}
