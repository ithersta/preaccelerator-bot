package ru.spbstu.preaccelerator.domain.entities

import kotlin.time.Duration

class StepConfig<S : Step>(
    val steps: List<S>
) {
    val fullDuration = steps.fold(Duration.ZERO) { acc, step -> acc + step.duration }
}

open class Step(
    val number: Number,
    val name: String,
    val duration: Duration
) {
    @JvmInline
    value class Number(val value: Int)
}
