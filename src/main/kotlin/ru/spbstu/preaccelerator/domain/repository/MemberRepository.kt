package ru.spbstu.preaccelerator.domain.repository

import ru.spbstu.preaccelerator.domain.entities.PhoneNumber
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.user.Member

interface MemberRepository {
    fun get(id: Member.Id): Member
    fun get(teamId: Team.Id): List<Member>
    fun get(phoneNumber: PhoneNumber): Member?
    fun add(phoneNumber: PhoneNumber, teamId: Team.Id): Member.Id
}
