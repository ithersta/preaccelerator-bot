package ru.spbstu.preaccelerator.domain.repository

import ru.spbstu.preaccelerator.domain.entities.Meeting
import ru.spbstu.preaccelerator.domain.entities.Protocol
import ru.spbstu.preaccelerator.domain.entities.user.Tracker

interface ProtocolRepository {
    fun get(meetingId: Meeting.Id): Protocol
    fun add(meetingId: Meeting.Id, url: String)
}
