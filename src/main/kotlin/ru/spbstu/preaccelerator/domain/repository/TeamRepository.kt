package ru.spbstu.preaccelerator.domain.repository

import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.user.Tracker

interface TeamRepository {
    fun getAll(): List<Team>
    fun get(id: Team.Id): Team
    fun get(trackerId: Tracker.Id): List<Team>
    fun get(name: String): Team?
    fun add(name: String, trackerId: Tracker.Id): Team.Id
    fun getPaginatedWithSentProtocols(offset: Int, limit: Int): List<Team>
    fun countWithSentProtocols(): Int
    fun getByTrackerIdPaginated(trackerId: Tracker.Id, offset: Int, limit: Int): List<Team>
    fun countByTrackerId(trackerId: Tracker.Id): Int
    fun getAllPaginated(offset: Int, limit: Int): List<Team>
    fun countAll(): Int
}
