package ru.spbstu.preaccelerator.domain.usecases.actions

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.domain.repository.TeamRepository
import ru.spbstu.preaccelerator.domain.repository.UserPhoneNumberRepository

@Single
class TrackerActions(
    private val teamRepository: TeamRepository,
    private val phoneNumberRepository: UserPhoneNumberRepository
) {
    val Tracker.teams get() = teamRepository.get(id)
    val Tracker.userId get() = phoneNumberRepository.get(phoneNumber)!!
}
