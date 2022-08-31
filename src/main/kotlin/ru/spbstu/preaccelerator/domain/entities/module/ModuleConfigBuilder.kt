package ru.spbstu.preaccelerator.domain.entities.module

import kotlin.time.Duration

class ModuleConfigBuilder {
    private val modules = mutableListOf<Module>()
    private val tasks = mutableListOf<Task>()

    fun module(
        name: String,
        duration: Duration,
        block: ModuleBuilder.() -> Unit
    ){
        modules.add(ModuleBuilder(Module.Number(modules.size), name, duration, tasks).apply(block).build())
    }

    fun build() = ModuleConfig(modules, tasks)
}

fun moduleConfig(block: ModuleConfigBuilder.() -> Unit): ModuleConfig {
    return ModuleConfigBuilder().apply(block).build()
}
