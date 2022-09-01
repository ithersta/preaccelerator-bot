package ru.spbstu.preaccelerator.domain.usecases.actions

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.data.DatabaseTransactionWithResult
import ru.spbstu.preaccelerator.domain.entities.PhoneNumber
import ru.spbstu.preaccelerator.domain.repository.TeamRepository
import ru.spbstu.preaccelerator.domain.repository.TrackerRepository

@Single
class AddTrackerAndTeamUseCase(
    private val trackerRepository: TrackerRepository,
    private val teamRepository: TeamRepository,
    private val transactionWithResult: DatabaseTransactionWithResult
) {
    operator fun invoke(mapOfTrackers: Map<PhoneNumber, String>) = transactionWithResult {
        mapOfTrackers.forEach { pair ->
            val tracker = trackerRepository.get(pair.key)
            val trackerId = if (tracker != null) {
                tracker.id
            } else {
                trackerRepository.add(pair.key)
            }
            teamRepository.add(pair.value, trackerId)
        }
        return@transactionWithResult mapOfTrackers.size
    }

}