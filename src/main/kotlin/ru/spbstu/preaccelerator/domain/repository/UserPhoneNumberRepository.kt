package ru.spbstu.preaccelerator.domain.repository

import dev.inmo.tgbotapi.types.UserId
import ru.spbstu.preaccelerator.domain.entities.PhoneNumber

interface UserPhoneNumberRepository {
    fun add(userId: UserId, phoneNumber: PhoneNumber)
    fun get(userId: UserId): PhoneNumber?
    fun get(phoneNumber: PhoneNumber): UserId?
}
