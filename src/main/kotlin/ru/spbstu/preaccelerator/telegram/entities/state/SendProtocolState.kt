package ru.spbstu.preaccelerator.telegram.entities.state

import kotlinx.serialization.Serializable

@Serializable
object SendProtocolState : DialogState

@Serializable
object ChooseMeetingState : DialogState

@Serializable
object UrlGoogleDocState : DialogState

@Serializable
object CheckCorrectInput : DialogState

@Serializable
object FinalSendProtocolState : DialogState

