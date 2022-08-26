package ru.spbstu.preaccelerator.domain.usecases

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.domain.entities.StepConfig
import ru.spbstu.preaccelerator.domain.repository.SeasonStartRepository
import java.time.OffsetDateTime
import kotlin.time.toJavaDuration

@Single
class IsSeasonActiveUseCase(
    private val seasonStartRepository: SeasonStartRepository,
    private val stepConfig: StepConfig
) {
    operator fun invoke(at: OffsetDateTime): Boolean {
        val start = seasonStartRepository.get() ?: return false
        return at.isAfter(start) && at.isBefore(start + stepConfig.fullDuration.toJavaDuration())
    }
}
