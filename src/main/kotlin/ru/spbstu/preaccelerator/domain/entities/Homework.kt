package ru.spbstu.preaccelerator.domain.entities

class Homework(
    val id: Id,
    val teamId: Team.Id,
    val url: String
) {
    @JvmInline
    value class Id(val value: Long)
}
