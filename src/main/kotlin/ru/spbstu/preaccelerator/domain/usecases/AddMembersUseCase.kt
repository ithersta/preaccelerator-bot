package ru.spbstu.preaccelerator.domain.usecases

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.data.DatabaseTransactionWithResult
import ru.spbstu.preaccelerator.domain.entities.PhoneNumber
import ru.spbstu.preaccelerator.domain.repository.MemberRepository
import ru.spbstu.preaccelerator.domain.repository.TeamRepository
import ru.spbstu.preaccelerator.domain.repository.TrackerRepository
import java.util.stream.Stream
import kotlin.streams.toList

@Single
class AddMembersUseCase(
    private val memberRepository: MemberRepository,
    private val teamRepository: TeamRepository,
    private val transactionWithResult: DatabaseTransactionWithResult
) {
    operator fun invoke(mapOfMembers: Map<PhoneNumber, String>) = transactionWithResult {
        val ans = Stream.builder<String>()
        mapOfMembers.forEach { pair ->
            val team = teamRepository.get(pair.value)
            if (team == null) {
                ans.add(pair.value)
            } else {
                memberRepository.add(pair.key, team.id)
            }
        }
        val list = ans.build().toList().toSet()
        return@transactionWithResult list.ifEmpty { null }
    }
}