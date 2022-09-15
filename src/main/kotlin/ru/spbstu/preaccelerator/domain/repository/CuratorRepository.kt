package ru.spbstu.preaccelerator.domain.repository

import dev.inmo.tgbotapi.types.UserId
import ru.spbstu.preaccelerator.domain.entities.user.Curator

interface CuratorRepository {
    fun isEmpty(): Boolean
    fun add(userId: UserId)
    fun get(userId: UserId): Curator?
    fun getAll(): List<Curator>
}
