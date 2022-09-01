package ru.spbstu.preaccelerator

import com.ithersta.tgbotapi.fsm.entities.StateMachine
import dev.inmo.tgbotapi.bot.ktor.telegramBot
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import org.koin.core.context.startKoin
import ru.spbstu.preaccelerator.telegram.DatabaseDumper

suspend fun main() {
    val application = startKoin { modules(preacceleratorModule) }
    val stateMachine: StateMachine<*, *, *> by application.koin.inject()
    val databaseDumper: DatabaseDumper by application.koin.inject()
    telegramBot(System.getenv("TOKEN")).buildBehaviourWithLongPolling {
        stateMachine.run { collect() }
        databaseDumper.run { start() }
    }.join()
}
