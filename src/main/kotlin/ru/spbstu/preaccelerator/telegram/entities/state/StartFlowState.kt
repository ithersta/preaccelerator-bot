package ru.spbstu.preaccelerator.telegram.entities.state

import kotlinx.serialization.Serializable

object StartFlowState {
    @Serializable
    object WaitingForContact : DialogState
}
