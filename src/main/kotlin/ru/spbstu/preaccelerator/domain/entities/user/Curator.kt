package ru.spbstu.preaccelerator.domain.entities.user

import dev.inmo.tgbotapi.types.UserId

class Curator(
    val userId: UserId
) : PreacceleratorUser {
    @JvmInline
    value class Id(val value: Long)
}
