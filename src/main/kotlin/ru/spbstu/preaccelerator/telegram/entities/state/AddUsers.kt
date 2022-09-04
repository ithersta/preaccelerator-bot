package ru.spbstu.preaccelerator.telegram.entities.state

import kotlinx.serialization.Serializable

object AddUsers {
    @Serializable
    object WaitingForDocument : DialogState
}
