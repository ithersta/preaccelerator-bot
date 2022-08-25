package ru.spbstu.preaccelerator.domain.entities.user

import ru.spbstu.preaccelerator.domain.entities.PhoneNumber

class EmptyUser(
    val setPhoneNumber: (PhoneNumber) -> Unit
) : PreacceleratorUser
