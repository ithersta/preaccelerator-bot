package ru.spbstu.preaccelerator.telegram.entities.state

import kotlinx.serialization.Serializable
import ru.spbstu.preaccelerator.domain.entities.module.Module

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
