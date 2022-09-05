package ru.spbstu.preaccelerator.domain.usecases

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.domain.DatabaseTransactionWithResult
import ru.spbstu.preaccelerator.domain.entities.PhoneNumber
import ru.spbstu.preaccelerator.domain.repository.MemberRepository
import ru.spbstu.preaccelerator.domain.repository.TeamRepository
import ru.spbstu.preaccelerator.domain.repository.TrackerRepository

@Single
class AddUsersUseCase(
    private val memberRepository: MemberRepository,
    private val trackerRepository: TrackerRepository,
    private val teamRepository: TeamRepository,
    private val transactionWithResult: DatabaseTransactionWithResult
) {
    class Result(
        val membersCount: Int,
        val teamsCount: Int,
        val notFoundTeams: List<String>
    )

    operator fun invoke(members: List<Pair<PhoneNumber, String>>, teams: List<Pair<PhoneNumber, String>>) =
        transactionWithResult {
            val teamsCount = teams.count { (trackerPhoneNumber, teamName) ->
                val trackerId = trackerRepository.add(trackerPhoneNumber)
                teamRepository.add(teamName, trackerId)
                true
            }
            val notFoundTeams = mutableSetOf<String>()
            val membersCount = members.count { (memberPhoneNumber, teamName) ->
                val team = teamRepository.get(teamName)
                if (team == null) {
                    notFoundTeams.add(teamName)
                    false
                } else {
                    memberRepository.add(memberPhoneNumber, team.id)
                    true
                }
            }
            Result(membersCount, teamsCount, notFoundTeams.toList())
        }

}
