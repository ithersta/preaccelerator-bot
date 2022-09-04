package ru.spbstu.preaccelerator.telegram.entities.state

import kotlinx.serialization.Serializable

object LoadListOfUsersState {
    @Serializable
    object WaitingForDocument: DialogState
}