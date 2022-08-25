package ru.spbstu.preaccelerator.domain.entities

import ru.spbstu.preaccelerator.domain.entities.user.Member
import ru.spbstu.preaccelerator.domain.entities.user.Tracker

class Team(
    val id: Id,
    val name: String,
    val loadTracker: () -> Tracker,
    val loadMembers: () -> List<Member>
) {
    @JvmInline
    value class Id(val value: Long)
}
