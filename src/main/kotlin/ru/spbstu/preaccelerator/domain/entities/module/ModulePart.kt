package ru.spbstu.preaccelerator.domain.entities.module

class ModulePart (
    val name: String,
    val speaker: String,
    val lectionURL: String,
    val presentationURL: String,
    val additionalInfo: Optional<String>,
    val task: Optional<Task>
    )