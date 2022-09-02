package ru.spbstu.preaccelerator.domain.repository

import ru.spbstu.preaccelerator.domain.entities.Homework
import ru.spbstu.preaccelerator.domain.entities.Team

interface HomeworkRepository {
    fun get(teamId: Team.Id): List<Homework>
}
