package ru.spbstu.preaccelerator.data.repository

import dev.inmo.tgbotapi.types.UserId
import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.data.AppDatabase
import ru.spbstu.preaccelerator.domain.entities.PhoneNumber
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.user.*
import ru.spbstu.preaccelerator.domain.repository.UserRepository

@Single
class UserRepositoryImpl(
    private val appDatabase: AppDatabase
) : UserRepository {
    override fun get(userId: UserId): PreacceleratorUser {
        getCurator(userId)?.let { return it }
        return getPhoneNumber(userId)?.let {
            getUser(it)
        } ?: return createEmptyUser(userId)
    }

    fun getCurator(userId: UserId): Curator? {
        return appDatabase.curatorQueries.get(userId).executeAsOneOrNull()?.toCurator()
    }

    fun getPhoneNumber(userId: UserId): PhoneNumber? {
        return appDatabase.userPhoneNumberQueries.get(userId).executeAsOneOrNull()
    }

    fun getUser(phoneNumber: PhoneNumber): PreacceleratorUser? {
        getMember(phoneNumber)?.let { return it }
        getTracker(phoneNumber)?.let { return it }
        return null
    }

    fun getTracker(phoneNumber: PhoneNumber): Tracker? {
        return appDatabase.trackerQueries.getByPhoneNumber(phoneNumber).executeAsOneOrNull()?.toTracker()
    }

    fun getMember(phoneNumber: PhoneNumber): Member? {
        return appDatabase.memberQueries.getByPhoneNumber(phoneNumber).executeAsOneOrNull()?.toMember()
    }

    fun getTeam(id: Team.Id): Team {
        return appDatabase.teamQueries.get(id).executeAsOne().toTeam()
    }

    fun getTracker(id: Tracker.Id): Tracker {
        return appDatabase.trackerQueries.get(id).executeAsOne().toTracker()
    }

    fun getMembers(id: Team.Id): List<Member> {
        return appDatabase.memberQueries.getByTeamId(id).executeAsList().map { it.toMember() }
    }

    fun getTeams(id: Tracker.Id): List<Team> {
        return appDatabase.teamQueries.getByTrackerId(id).executeAsList().map { it.toTeam() }
    }

    private fun setPhoneNumber(userId: UserId, phoneNumber: PhoneNumber) {
        appDatabase.userPhoneNumberQueries.add(userId, phoneNumber)
    }

    private fun UserId.toCurator() = Curator(
        userId = this
    )

    private fun ru.spbstu.preaccelerator.data.Tracker.toTracker() = Tracker(
        id = id,
        phoneNumber = phoneNumber,
        loadTeams = { getTeams(id) }
    )

    private fun ru.spbstu.preaccelerator.data.Member.toMember() = Member(
        id = id,
        phoneNumber = phoneNumber,
        loadTeam = { getTeam(teamId) }
    )

    private fun ru.spbstu.preaccelerator.data.Team.toTeam() = Team(
        id = id,
        name = name,
        loadTracker = { getTracker(trackerId) },
        loadMembers = { getMembers(id) }
    )

    private fun createEmptyUser(userId: UserId) = EmptyUser(
        setPhoneNumber = { setPhoneNumber(userId, it) }
    )
}
