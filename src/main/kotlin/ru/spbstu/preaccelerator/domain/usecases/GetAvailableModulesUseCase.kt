package ru.spbstu.preaccelerator.domain.usecases

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module
import ru.spbstu.preaccelerator.domain.entities.module.ModuleConfig
import ru.spbstu.preaccelerator.domain.repository.HomeworkRepository

@Single
class GetAvailableModulesUseCase(
    private val homeworkRepository: HomeworkRepository,
    private val moduleConfig: ModuleConfig
) {
    operator fun invoke(teamId: Team.Id): List<Module> {
        val homeworks = homeworkRepository.get(teamId)
        val modules = moduleConfig.modules
        return listOf(modules.first()) + modules.asSequence()
            .zipWithNext()
            .takeWhile { (module, _) ->
                module.tasks.all { task ->
                    homeworks.any { homework ->
                        homework.taskNumber == task.number
                    }
                }
            }
            .map { it.second }
    }
}
