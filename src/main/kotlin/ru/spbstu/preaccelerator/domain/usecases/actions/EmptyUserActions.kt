package ru.spbstu.preaccelerator.domain.usecases.actions

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.domain.entities.PhoneNumber
import ru.spbstu.preaccelerator.domain.entities.user.EmptyUser
import ru.spbstu.preaccelerator.domain.repository.UserPhoneNumberRepository
import ru.spbstu.preaccelerator.domain.usecases.CuratorTokenInteractor

@Single
class EmptyUserActions(
    private val userPhoneNumberRepository: UserPhoneNumberRepository,
    private val curatorTokenInteractor: CuratorTokenInteractor
) {
    fun EmptyUser.setPhoneNumber(phoneNumber: PhoneNumber) {
        userPhoneNumberRepository.add(userId, phoneNumber)
    }

    fun EmptyUser.useCuratorToken(token: String): Boolean {
        return curatorTokenInteractor.use(token, userId)
    }
}
