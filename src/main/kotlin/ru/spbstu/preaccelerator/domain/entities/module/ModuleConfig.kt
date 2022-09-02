package ru.spbstu.preaccelerator.domain.entities.module

import kotlinx.serialization.Serializable
import kotlin.time.Duration

class ModuleConfig(
    val modules: List<Module>,
    val tasks: List<Task>
) {
    val fullDuration = modules.fold(Duration.ZERO) { acc, module -> acc + module.duration }
}

class Module(
    val number: Number,
    val name: String,
    val duration: Duration,
    val finalTestUrl: String,
    val parts: List<ModulePart>
) {
    @JvmInline
    @Serializable
    value class Number(val value: Int)
}
