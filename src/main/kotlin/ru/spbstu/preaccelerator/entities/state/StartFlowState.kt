package ru.spbstu.preaccelerator.entities.state

import kotlinx.serialization.Serializable

object StartFlowState {
    @Serializable
    object WaitingForContact : DialogState
}
