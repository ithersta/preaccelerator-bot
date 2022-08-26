package ru.spbstu.preaccelerator.domain.entities.user

import dev.inmo.tgbotapi.types.UserId

class EmptyUser(
    val userId: UserId
) : PreacceleratorUser
