package ru.spbstu.preaccelerator.telegram.entities.state

import kotlinx.serialization.Serializable

object SendInfoState {
    @Serializable
    sealed interface BaseState : DialogState
    @Serializable
    object AllFromCurator : BaseState
    @Serializable
    object TrackersFromCurator : BaseState
    @Serializable
    object TeamsFromCurator : BaseState
}