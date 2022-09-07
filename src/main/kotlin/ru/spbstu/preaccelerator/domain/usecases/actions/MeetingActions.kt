package ru.spbstu.preaccelerator.domain.usecases.actions

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.domain.entities.Meeting
import ru.spbstu.preaccelerator.domain.repository.ProtocolRepository

@Single
class MeetingActions(
    private val protocolRepository: ProtocolRepository
) {
    val Meeting.protocol get() = protocolRepository.get(id)
}