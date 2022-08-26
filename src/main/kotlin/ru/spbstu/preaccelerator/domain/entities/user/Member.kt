package ru.spbstu.preaccelerator.domain.entities.user

import ru.spbstu.preaccelerator.domain.entities.PhoneNumber
import ru.spbstu.preaccelerator.domain.entities.Team

class Member(
    val id: Id,
    val phoneNumber: PhoneNumber,
    val teamId: Team.Id
) : PreacceleratorUser {
    @JvmInline
    value class Id(val value: Long)
}
