package ru.spbstu.preaccelerator.domain.entities

import ru.spbstu.preaccelerator.domain.entities.user.Tracker

class Team(
    val id: Id,
    val name: String,
    val trackerId: Tracker.Id
) {
    @JvmInline
    value class Id(val value: Long)
}
