package ru.spbstu.preaccelerator.domain.usecases

import dev.inmo.tgbotapi.types.UserId
import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.domain.entities.user.EmptyUser
import ru.spbstu.preaccelerator.domain.entities.user.PreacceleratorUser
import ru.spbstu.preaccelerator.domain.repository.CuratorRepository
import ru.spbstu.preaccelerator.domain.repository.MemberRepository
import ru.spbstu.preaccelerator.domain.repository.TrackerRepository
import ru.spbstu.preaccelerator.domain.repository.UserPhoneNumberRepository
import java.time.OffsetDateTime

@Single
class GetUserUseCase(
    private val userPhoneNumberRepository: UserPhoneNumberRepository,
    private val memberRepository: MemberRepository,
    private val trackerRepository: TrackerRepository,
    private val curatorRepository: CuratorRepository,
    private val isSeasonActive: IsSeasonActiveUseCase
) {
    operator fun invoke(userId: UserId, at: OffsetDateTime): PreacceleratorUser {
        curatorRepository.get(userId)?.let { return it }
        if (isSeasonActive(at)) {
            userPhoneNumberRepository.get(userId)?.let { phoneNumber ->
                trackerRepository.get(phoneNumber)?.let { return it }
                memberRepository.get(phoneNumber)?.let { return it }
            }
        }
        return EmptyUser(userId)
    }
}
