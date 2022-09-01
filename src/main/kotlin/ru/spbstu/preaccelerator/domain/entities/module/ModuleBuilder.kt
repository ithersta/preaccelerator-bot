package ru.spbstu.preaccelerator.domain.entities.module

import kotlin.time.Duration

class ModuleBuilder(
    private val number: Module.Number,
    private val name: String,
    private val duration: Duration,
    private val tasks: MutableList<Task>
) {
    private val parts = mutableListOf<ModulePart>()

    fun task(description: String) {
        val task = Task(Task.Number(tasks.size), description)
        tasks.add(task)
        parts.add(task)
    }

    fun lecture(
        name: String,
        speaker: String,
        url: String,
        presentationUrl: String
    ) {
        parts.add(Lecture(name, speaker, url, presentationUrl))
    }

    fun additionalInfo(text: String) {
        parts.add(AdditionalInfo(text))
    }

    fun build() = Module(number, name, duration, parts)
}
