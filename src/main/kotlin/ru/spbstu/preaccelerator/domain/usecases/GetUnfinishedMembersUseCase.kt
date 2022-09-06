package ru.spbstu.preaccelerator.domain.usecases

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.domain.entities.module.Module
import ru.spbstu.preaccelerator.domain.entities.user.Member
import ru.spbstu.preaccelerator.domain.repository.TeamRepository
import ru.spbstu.preaccelerator.domain.usecases.actions.TeamActions

@Single
class GetUnfinishedMembersUseCase(
    private val teamRepository: TeamRepository,
    private val teamActions: TeamActions
) {
    operator fun invoke(module: Module): List<Member> = with(teamActions) {
        return teamRepository.getAll().filterNot { team ->
            module.tasks.all { task ->
                team.getHomework(task.number) != null
            }
        }.flatMap { it.members }
    }
}
