package ru.spbstu.preaccelerator.telegram.entities.state

import kotlinx.serialization.Serializable

object StartFlowState {
    @Serializable
    object WaitingForContact : DialogState

    @Serializable
    object WaitingForInputDate : DialogState

    @Serializable
    object AfterAuthenticating : DialogState

    @Serializable
    object WaitingForStartSeason : DialogState
}
