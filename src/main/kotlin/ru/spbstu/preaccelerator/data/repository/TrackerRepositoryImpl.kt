package ru.spbstu.preaccelerator.data.repository

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.data.AppDatabase
import ru.spbstu.preaccelerator.domain.entities.PhoneNumber
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.domain.repository.TrackerRepository

@Single
class TrackerRepositoryImpl(
    private val appDatabase: AppDatabase
) : TrackerRepository {
    override fun get(id: Tracker.Id): Tracker {
        return appDatabase.trackerQueries.get(id).executeAsOne().toDomainModel()
    }

    override fun get(phoneNumber: PhoneNumber): Tracker? {
        return appDatabase.trackerQueries.getByPhoneNumber(phoneNumber).executeAsOneOrNull()?.toDomainModel()
    }

    override fun add(phoneNumber: PhoneNumber): Tracker.Id {
        return appDatabase.trackerQueries.add(phoneNumber).executeAsOne()
    }

    private fun ru.spbstu.preaccelerator.data.Tracker.toDomainModel() = Tracker(
        id = id,
        phoneNumber = phoneNumber
    )
}
