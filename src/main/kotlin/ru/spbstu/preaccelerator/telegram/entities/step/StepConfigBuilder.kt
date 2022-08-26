package ru.spbstu.preaccelerator.telegram.entities.step

import com.ithersta.tgbotapi.fsm.StatefulContext
import dev.inmo.tgbotapi.types.message.content.TextMessage
import ru.spbstu.preaccelerator.domain.entities.Step
import ru.spbstu.preaccelerator.domain.entities.StepConfig
import ru.spbstu.preaccelerator.domain.entities.user.Member
import ru.spbstu.preaccelerator.domain.entities.user.PreacceleratorUser
import ru.spbstu.preaccelerator.telegram.entities.state.DialogState
import kotlin.time.Duration

class StepConfigBuilder {
    private val steps = mutableListOf<TelegramStep>()

    fun step(
        name: String,
        duration: Duration,
        onStart: suspend StatefulContext<DialogState, PreacceleratorUser, *, Member>.(TextMessage) -> Unit
    ) {
        steps.add(TelegramStep(Step.Number(steps.size), name, duration, onStart))
    }

    fun build() = StepConfig(steps)
}

fun stepConfig(block: StepConfigBuilder.() -> Unit): StepConfig<TelegramStep> {
    return StepConfigBuilder().apply(block).build()
}
