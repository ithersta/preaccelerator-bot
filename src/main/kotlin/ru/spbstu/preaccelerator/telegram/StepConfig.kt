package ru.spbstu.preaccelerator.telegram

import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import ru.spbstu.preaccelerator.telegram.entities.step.stepConfig
import kotlin.time.Duration.Companion.days

val stepConfig = stepConfig {
    step("Модуль 1", 7.days) {
        sendTextMessage(it.chat, "Выбран модуль 1")
    }
    step("Модуль 2", 7.days) {
        sendTextMessage(it.chat, "Выбран модуль 2")
    }
    step("Модуль 3", 7.days) {
        sendTextMessage(it.chat, "Выбран модуль 3")
    }
}
