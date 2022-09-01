package ru.spbstu.preaccelerator.telegram

import ru.spbstu.preaccelerator.domain.entities.module.moduleConfig
import ru.spbstu.preaccelerator.telegram.resources.modules.*

fun createModuleConfig() = moduleConfig {
    firstModule()
    secondModule()
    thirdModule()
    fourthModule()
    fifthModule()
    sixthModule()
    seventhModule()
    eighthModule()
}
