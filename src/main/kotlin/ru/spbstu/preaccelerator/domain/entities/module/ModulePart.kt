package ru.spbstu.preaccelerator.domain.entities.module

sealed interface ModulePart

class Lecture(
    val name: String,
    val speaker: String,
    val url: String,
    val presentationUrl: String,
): ModulePart

class Task(
    val number: Number,
    val description: String
): ModulePart {
    @JvmInline
    value class Number(val value: Int)
}

class AdditionalInfo(
    val text: String
): ModulePart