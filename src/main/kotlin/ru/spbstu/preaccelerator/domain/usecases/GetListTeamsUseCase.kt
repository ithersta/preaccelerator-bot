package ru.spbstu.preaccelerator.domain.usecases

import dev.inmo.tgbotapi.types.UserId
import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.user.Curator
import ru.spbstu.preaccelerator.domain.entities.user.PreacceleratorUser
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.domain.repository.TeamRepository
import ru.spbstu.preaccelerator.domain.repository.TrackerRepository
import ru.spbstu.preaccelerator.domain.repository.UserPhoneNumberRepository

@Single
class GetListTeamsUseCase(
    private val teamRepository: TeamRepository,
    private val userPhoneNumberRepository: UserPhoneNumberRepository,
    private val trackerRepository: TrackerRepository
) {
    operator fun invoke(user: PreacceleratorUser, userId: UserId): List<Team> =
        when (user) {
            is Curator -> teamRepository.getAll()
            is Tracker -> teamRepository.get(trackerRepository.get(userPhoneNumberRepository.get(userId)!!)!!.id)
            else -> {
                error("user is not curator or tracker")
            }
        }
}