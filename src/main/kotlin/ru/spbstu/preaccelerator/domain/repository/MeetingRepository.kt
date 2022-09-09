package ru.spbstu.preaccelerator.domain.repository

import ru.spbstu.preaccelerator.domain.entities.Meeting
import ru.spbstu.preaccelerator.domain.entities.module.Module
import ru.spbstu.preaccelerator.domain.entities.user.Tracker

interface MeetingRepository {
    fun get(trackerId: Tracker.Id, moduleNumber: Module.Number): List<Meeting>
}
