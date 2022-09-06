package ru.spbstu.preaccelerator.telegram.entities.state

import kotlinx.serialization.Serializable

@Serializable
object NewMeeting : DialogState

@Serializable
object URLMeeting : DialogState

@Serializable
object TimeMeeting : DialogState

@Serializable
object CheckCorrect : DialogState
