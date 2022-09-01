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
        mapOfTeams.forEach { pair ->
            val tracker = trackerRepository.get(pair.key)
            val trackerId = if (tracker != null) {
                tracker.id
            } else {
                trackerRepository.add(pair.key)
            }
            teamRepository.add(pair.value, trackerId)
        }
        val setOfNotFindTeams = mutableSetOf<String>()
        mapOfMembers.forEach { pair ->
            val team = teamRepository.get(pair.value)
            if (team == null) {
                setOfNotFindTeams.add(pair.value)
            } else {
                memberRepository.add(pair.key, team.id)
            }
        }
        return@transactionWithResult setOfNotFindTeams
    }

}