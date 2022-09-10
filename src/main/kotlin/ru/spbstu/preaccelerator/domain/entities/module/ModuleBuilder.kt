package ru.spbstu.preaccelerator.domain.entities.module

import kotlin.time.Duration

class ModuleBuilder(
    private val number: Module.Number,
    private val name: String,
    private val duration: Duration,
    private val finalTestUrl: String,
    private val allTasks: MutableList<Task>
) {
    private val parts = mutableListOf<ModulePart>()

    fun task(description: String, templateName: String) {
        val task = Task(Task.Number(allTasks.size), description, templateName)
        allTasks.add(task)
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

    fun build() = Module(number, name, duration, finalTestUrl, parts, parts.filterIsInstance<Task>())
}
