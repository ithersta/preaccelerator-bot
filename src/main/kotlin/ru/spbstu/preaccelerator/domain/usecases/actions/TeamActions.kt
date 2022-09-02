package ru.spbstu.preaccelerator.domain.usecases.actions

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.repository.MemberRepository
import ru.spbstu.preaccelerator.domain.usecases.GetAvailableModulesUseCase

@Single
class TeamActions(
    private val memberRepository: MemberRepository,
    private val getAvailableModules: GetAvailableModulesUseCase
) {
    val Team.members get() = memberRepository.get(id)
    val Team.availableModules get() = getAvailableModules(id)
}
