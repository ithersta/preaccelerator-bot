package ru.spbstu.preaccelerator.domain.usecases.actions

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.Module
import ru.spbstu.preaccelerator.domain.entities.module.Task
import ru.spbstu.preaccelerator.domain.repository.HomeworkRepository
import ru.spbstu.preaccelerator.domain.repository.MeetingRepository
import ru.spbstu.preaccelerator.domain.repository.MemberRepository
import ru.spbstu.preaccelerator.domain.repository.ProtocolRepository
import ru.spbstu.preaccelerator.domain.repository.*
import ru.spbstu.preaccelerator.domain.usecases.GetAvailableModulesUseCase
import java.time.OffsetDateTime

@Single
class TeamActions(
    private val memberRepository: MemberRepository,
    private val homeworkRepository: HomeworkRepository,
    private val getAvailableModules: GetAvailableModulesUseCase,
    private val meetingRepository: MeetingRepository,
    private val protocolRepository: ProtocolRepository,
    private val protocolStatusRepository: ProtocolStatusRepository
) {
    val Team.protocol get() = protocolRepository.get(id)
    val Team.members get() = memberRepository.get(id)
    val Team.meetings get() = meetingRepository.get(id)
    val Team.availableModules get() = getAvailableModules(id)

    fun Team.addHomework(taskNumber: Task.Number, url: String, at: OffsetDateTime) =
        homeworkRepository.add(id, taskNumber, url, at)

    fun Team.getHomework(taskNumber: Task.Number) =
        homeworkRepository.get(id, taskNumber)

    fun Team.getProtocolStatus(moduleNumber: Module.Number) =
        protocolStatusRepository.get(id, moduleNumber)
}
