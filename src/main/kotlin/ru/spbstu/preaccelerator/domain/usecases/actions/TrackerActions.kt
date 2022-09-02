package ru.spbstu.preaccelerator.domain.usecases.actions

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.domain.repository.TeamRepository

@Single
class TrackerActions(
    private val teamRepository: TeamRepository
) {
    val Tracker.teams get() = teamRepository.get(id)
}
