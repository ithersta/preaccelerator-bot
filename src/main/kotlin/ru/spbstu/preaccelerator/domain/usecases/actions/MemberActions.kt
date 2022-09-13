package ru.spbstu.preaccelerator.domain.usecases.actions

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.domain.entities.user.Member
import ru.spbstu.preaccelerator.domain.repository.TeamRepository
import ru.spbstu.preaccelerator.domain.repository.UserPhoneNumberRepository

@Single
class MemberActions(
    private val teamRepository: TeamRepository,
    private val phoneNumberRepository: UserPhoneNumberRepository
) {
    val Member.team get() = teamRepository.get(teamId)
    val Member.userId get() = phoneNumberRepository.get(phoneNumber)
}
