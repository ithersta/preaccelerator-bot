package ru.spbstu.preaccelerator.domain.entities.module

import kotlinx.serialization.Serializable
import kotlin.time.Duration

class ModuleConfig(
    val modules: Map<Module.Number, Module>,
    val tasks: List<Task>
) {
    val fullDuration = modules.values.fold(Duration.ZERO) { acc, module -> acc + module.duration }
}

class Module(
    val number: Number,
    val name: String,
    val duration: Duration,
    val finalTestUrl: String,
    val parts: List<ModulePart>,
    val tasks: List<Task>
) {
    @JvmInline
    @Serializable
    value class Number(val value: Int)
}

val Module.lectures get() = parts.filterIsInstance<Lecture>()
