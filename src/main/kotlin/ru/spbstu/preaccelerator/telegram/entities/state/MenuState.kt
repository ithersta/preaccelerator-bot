package ru.spbstu.preaccelerator.telegram.entities.state

import kotlinx.serialization.Serializable

object MenuState {
    object Curator {
        @Serializable
        object SendInfo : DialogState

        @Serializable
        object GetStats : DialogState

        @Serializable
        object AddUsers : DialogState
    }

    object Tracker {
        @Serializable
        object Meetings : DialogState

        @Serializable
        object Teams : DialogState
    }
}
