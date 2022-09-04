package ru.spbstu.preaccelerator.domain.usecases.actions

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.domain.DatabaseTransactionWithResult
import ru.spbstu.preaccelerator.domain.entities.PhoneNumber
import ru.spbstu.preaccelerator.domain.repository.MemberRepository
import ru.spbstu.preaccelerator.domain.repository.TeamRepository
import ru.spbstu.preaccelerator.domain.repository.TrackerRepository

@Single
class AddTrackerTeamAndMemberUseCase(
    private val memberRepository: MemberRepository,
    private val trackerRepository: TrackerRepository,
    private val teamRepository: TeamRepository,
    private val transactionWithResult: DatabaseTransactionWithResult
) {
    operator fun invoke(mapOfTeams: Map<PhoneNumber, String>, mapOfMembers: Map<PhoneNumber, String>) = transactionWithResult {
        mapOfTeams.forEach { (trackerPhoneNumber, teamName) ->
            val trackerId = trackerRepository.add(trackerPhoneNumber)
            teamRepository.add(teamName, trackerId)
        }
        val notFoundTeams = mutableSetOf<String>()
        mapOfMembers.forEach { (memberPhoneNumber, teamName) ->
            val team = teamRepository.get(teamName)
            if (team == null) {
                notFoundTeams.add(teamName)
            } else {
                memberRepository.add(memberPhoneNumber, team.id)
            }
        }
        return@transactionWithResult notFoundTeams
    }

}