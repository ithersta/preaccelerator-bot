package ru.spbstu.preaccelerator.domain.repository

import ru.spbstu.preaccelerator.domain.entities.PhoneNumber
import ru.spbstu.preaccelerator.domain.entities.user.Tracker

interface TrackerRepository {
    fun get(id: Tracker.Id): Tracker
    fun get(phoneNumber: PhoneNumber): Tracker?
    fun add(phoneNumber: PhoneNumber): Tracker.Id
}
