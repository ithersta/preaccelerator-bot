package ru.spbstu.preaccelerator.domain.entities.module

import kotlin.time.Duration

class ModuleConfig(
    val modules: List<Module>,
    val tasks: List<Task>
) {
    val fullDuration = modules.fold(Duration.ZERO) { acc, module -> acc + module.duration }
}

class Module (
    val number: Number,
    val name: String,
    val duration: Duration,
    val parts: List<ModulePart>
){
    @JvmInline
    value class Number(val value: Int)
}
