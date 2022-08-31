package ru.spbstu.preaccelerator.telegram.modules.entities

import java.util.Optional

class ModulePart (
    val name: String,
    val speaker: String,
    val lectionURL: String,
    val presentationURL: String,
    val additionalInfo: Optional<String>,
    val task: Optional<Task>
    )