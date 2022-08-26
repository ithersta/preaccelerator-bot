package ru.spbstu.preaccelerator.domain.usecases.actions

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.domain.entities.user.Member
import ru.spbstu.preaccelerator.domain.repository.TeamRepository

@Single
class MemberActions(
    private val teamRepository: TeamRepository
) {
    val Member.team get() = teamRepository.get(teamId)
}
