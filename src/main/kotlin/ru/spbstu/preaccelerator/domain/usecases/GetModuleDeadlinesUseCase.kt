package ru.spbstu.preaccelerator.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.domain.entities.module.Module
import ru.spbstu.preaccelerator.domain.entities.module.ModuleConfig
import ru.spbstu.preaccelerator.domain.repository.SeasonStartRepository
import java.time.OffsetDateTime
import kotlin.time.Duration
import kotlin.time.toJavaDuration

@Single
class GetModuleDeadlinesUseCase(
    private val seasonStartRepository: SeasonStartRepository,
    private val moduleConfig: ModuleConfig
) {
    operator fun invoke(): Flow<List<Pair<Module, OffsetDateTime>>> {
        val modules = moduleConfig.modules
        return seasonStartRepository.getAsFlow().map { start ->
            if (start == null) return@map emptyList()
            modules.mapIndexed { index, module ->
                val cumulativeDuration =
                    modules.take(index + 1).fold(Duration.ZERO) { acc, it -> acc + it.duration }
                val deadline = start + cumulativeDuration.toJavaDuration()
                module to deadline
            }
        }
    }
}
