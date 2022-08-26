package ru.spbstu.preaccelerator.domain.usecases.actions

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.repository.MemberRepository

@Single
class TeamActions(
    private val memberRepository: MemberRepository
) {
    val Team.members get() = memberRepository.get(id)
}
