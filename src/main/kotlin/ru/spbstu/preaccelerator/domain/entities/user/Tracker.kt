package ru.spbstu.preaccelerator.domain.entities.user

import ru.spbstu.preaccelerator.domain.entities.PhoneNumber
import ru.spbstu.preaccelerator.domain.entities.Team

class Tracker(
    val id: Id,
    val phoneNumber: PhoneNumber
) : PreacceleratorUser {
    @JvmInline
    value class Id(val value: Long)
}
