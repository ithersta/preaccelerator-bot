package ru.spbstu.preaccelerator

import dev.inmo.tgbotapi.bot.ktor.telegramBot
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import org.koin.core.context.startKoin

suspend fun main() {
    startKoin { modules(preacceleratorModule) }
    telegramBot(System.getenv("TOKEN")).buildBehaviourWithLongPolling {
        stateMachine.run { collect() }
    }.join()
}
