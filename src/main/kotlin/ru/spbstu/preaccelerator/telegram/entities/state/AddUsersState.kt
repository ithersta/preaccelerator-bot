package ru.spbstu.preaccelerator.telegram.entities.state

import kotlinx.serialization.Serializable

object AddUsersState {
    @Serializable
    object WaitingForDocument : DialogState
}
