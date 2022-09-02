package ru.spbstu.preaccelerator.domain.usecases.actions

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Task
import ru.spbstu.preaccelerator.domain.repository.HomeworkRepository
import ru.spbstu.preaccelerator.domain.repository.MemberRepository
import ru.spbstu.preaccelerator.domain.usecases.GetAvailableModulesUseCase
import java.time.OffsetDateTime

@Single
class TeamActions(
    private val memberRepository: MemberRepository,
    private val homeworkRepository: HomeworkRepository,
    private val getAvailableModules: GetAvailableModulesUseCase
) {
    val Team.members get() = memberRepository.get(id)
    val Team.availableModules get() = getAvailableModules(id)

    fun Team.addHomework(taskNumber: Task.Number, url: String, at: OffsetDateTime) =
        homeworkRepository.add(id, taskNumber, url, at)

    fun Team.isHomeworkDone(taskNumber: Task.Number) =
        homeworkRepository.get(id, taskNumber) != null
}
