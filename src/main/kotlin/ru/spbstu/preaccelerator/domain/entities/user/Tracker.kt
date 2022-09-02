package ru.spbstu.preaccelerator.domain.entities.user

import ru.spbstu.preaccelerator.domain.entities.PhoneNumber

class Tracker(
    val id: Id,
    val phoneNumber: PhoneNumber
) : PreacceleratorUser {
    @JvmInline
    value class Id(val value: Long)
}
