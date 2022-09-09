package ru.spbstu.preaccelerator.data.repository

import dev.inmo.tgbotapi.types.UserId
import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.data.AppDatabase
import ru.spbstu.preaccelerator.domain.entities.PhoneNumber
import ru.spbstu.preaccelerator.domain.repository.UserPhoneNumberRepository

@Single
class UserPhoneNumberRepositoryImpl(
    private val appDatabase: AppDatabase
) : UserPhoneNumberRepository {
    override fun add(userId: UserId, phoneNumber: PhoneNumber) {
        appDatabase.userPhoneNumberQueries.add(userId, phoneNumber)
    }

    override fun get(userId: UserId): PhoneNumber? {
        return appDatabase.userPhoneNumberQueries.get(userId).executeAsOneOrNull()
    }

    override fun get(phoneNumber: PhoneNumber): UserId? {
        return appDatabase.userPhoneNumberQueries.getUserId(phoneNumber).executeAsOneOrNull()
    }
}
