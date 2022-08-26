package ru.spbstu.preaccelerator.domain.repository

import dev.inmo.tgbotapi.types.UserId
import ru.spbstu.preaccelerator.domain.entities.user.Curator

interface CuratorRepository {
    fun get(userId: UserId): Curator?
}
