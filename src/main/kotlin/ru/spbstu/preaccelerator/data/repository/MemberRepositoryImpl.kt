package ru.spbstu.preaccelerator.data.repository

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.data.AppDatabase
import ru.spbstu.preaccelerator.domain.entities.PhoneNumber
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.user.Member
import ru.spbstu.preaccelerator.domain.repository.MemberRepository

@Single
class MemberRepositoryImpl(
    private val appDatabase: AppDatabase
) : MemberRepository {
    override fun get(id: Member.Id): Member {
        return appDatabase.memberQueries.get(id).executeAsOne().toDomainModel()
    }

    override fun get(teamId: Team.Id): List<Member> {
        return appDatabase.memberQueries.getByTeamId(teamId).executeAsList().map { it.toDomainModel() }
    }

    override fun get(phoneNumber: PhoneNumber): Member? {
        return appDatabase.memberQueries.getByPhoneNumber(phoneNumber).executeAsOneOrNull()?.toDomainModel()
    }

    override fun getAll(): List<Member> {
        return appDatabase.memberQueries.getAll().executeAsList().map { it.toDomainModel() }
    }

    override fun add(phoneNumber: PhoneNumber, teamId: Team.Id): Member.Id {
        return appDatabase.memberQueries.add(phoneNumber, teamId).executeAsOne()
    }

    private fun ru.spbstu.preaccelerator.data.Member.toDomainModel() = Member(
        id = id,
        phoneNumber = phoneNumber,
        teamId = teamId
    )
}
