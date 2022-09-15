package ru.spbstu.preaccelerator.domain.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.spbstu.preaccelerator.domain.entities.user.Tracker

class Team(
    val id: Id,
    val name: String,
    val trackerId: Tracker.Id
) {
    @Serializable
    @JvmInline
    value class Id(val value: Long)
}
