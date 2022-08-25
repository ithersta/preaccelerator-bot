package ru.spbstu.preaccelerator.domain.repository

import dev.inmo.tgbotapi.types.UserId
import ru.spbstu.preaccelerator.domain.entities.user.PreacceleratorUser

interface UserRepository {
    fun get(userId: UserId): PreacceleratorUser
}
