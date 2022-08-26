package ru.spbstu.preaccelerator.telegram.entities.step

import com.ithersta.tgbotapi.fsm.StatefulContext
import dev.inmo.tgbotapi.types.message.content.TextMessage
import ru.spbstu.preaccelerator.domain.entities.Step
import ru.spbstu.preaccelerator.domain.entities.user.Member
import ru.spbstu.preaccelerator.domain.entities.user.PreacceleratorUser
import ru.spbstu.preaccelerator.telegram.entities.state.DialogState
import kotlin.time.Duration

class TelegramStep(
    number: Number,
    name: String,
    duration: Duration,
    onStart: suspend StatefulContext<DialogState, PreacceleratorUser, *, Member>.(TextMessage) -> Unit
) : Step(number, name, duration)
