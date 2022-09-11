package ru.spbstu.preaccelerator.domain.entities

import kotlinx.serialization.Serializable

@Serializable
class Protocol(
    val teamId: Team.Id,
    val url: String
)
