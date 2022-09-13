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
import ru.spbstu.preaccelerator.telegram.extensions.TrackerExt.teams

@Single
class GetListTeamsUseCase(
    private val teamRepository: TeamRepository,
) {
    operator fun invoke(user: PreacceleratorUser): List<Team> =
        when (user) {
            is Curator -> teamRepository.getAll()
            is Tracker -> user.teams
            else -> {
                error("user is not curator or tracker")
            }
        }
}