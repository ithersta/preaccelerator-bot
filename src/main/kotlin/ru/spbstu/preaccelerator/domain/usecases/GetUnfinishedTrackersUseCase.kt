package ru.spbstu.preaccelerator.domain.usecases

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.domain.entities.isFinished
import ru.spbstu.preaccelerator.domain.entities.module.Module
import ru.spbstu.preaccelerator.domain.entities.module.ModuleConfig
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.domain.repository.MeetingRepository
import ru.spbstu.preaccelerator.domain.repository.ProtocolStatusRepository
import ru.spbstu.preaccelerator.domain.repository.TrackerRepository
import ru.spbstu.preaccelerator.domain.usecases.actions.TrackerActions
import java.time.OffsetDateTime
import kotlin.time.Duration
import kotlin.time.toJavaDuration

@Single
class GetUnfinishedTrackersUseCase(
    private val moduleConfig: ModuleConfig,
    private val protocolStatusRepository: ProtocolStatusRepository,
    private val meetingRepository: MeetingRepository,
    private val trackerRepository: TrackerRepository,
    private val trackerActions: TrackerActions
) {
    operator fun invoke(duration: Duration, at: OffsetDateTime): List<Pair<Tracker, Module.Number>> =
        with(trackerActions) {
            val tomorrow = at.plusDays(1)
            return trackerRepository.getAll()
                .flatMap { tracker ->
                    moduleConfig.modules
                        .filterNot { module ->
                            tracker.teams.all { protocolStatusRepository.get(it.id, module.number).isFinished() }
                        }
                        .mapNotNull { meetingRepository.getFirst(tracker.id, it.number) }
                        .filter {
                            val deadline = it.timestamp + duration.toJavaDuration()
                            deadline.isAfter(at) && deadline.isBefore(tomorrow)
                        }
                        .map { tracker to it.moduleNumber }
                }
        }
}
