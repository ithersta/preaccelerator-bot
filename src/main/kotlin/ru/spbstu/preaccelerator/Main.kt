package ru.spbstu.preaccelerator

import com.ithersta.tgbotapi.fsm.entities.StateMachine
import dev.inmo.tgbotapi.bot.ktor.telegramBot
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import org.koin.core.context.startKoin
import ru.spbstu.preaccelerator.telegram.CreateInitialCuratorToken
import ru.spbstu.preaccelerator.telegram.DatabaseDumper
import ru.spbstu.preaccelerator.telegram.notifications.Notifiers

suspend fun main() {
    val application = startKoin { modules(preacceleratorModule) }
    val stateMachine: StateMachine<*, *, *> by application.koin.inject()
    val databaseDumper: DatabaseDumper by application.koin.inject()
    val createInitialCuratorToken: CreateInitialCuratorToken by application.koin.inject()
    val notifiers: Notifiers by application.koin.inject()
    telegramBot(System.getenv("TOKEN")).buildBehaviourWithLongPolling {
        stateMachine.run { collect() }
        databaseDumper.run { start() }
        createInitialCuratorToken.run { invoke() }
        notifiers.run { start() }
    }.join()
}
