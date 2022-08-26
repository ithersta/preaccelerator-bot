package ru.spbstu.preaccelerator.domain.usecases.actions

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.domain.entities.PhoneNumber
import ru.spbstu.preaccelerator.domain.entities.user.EmptyUser
import ru.spbstu.preaccelerator.domain.repository.UserPhoneNumberRepository

@Single
class EmptyUserActions(
    private val userPhoneNumberRepository: UserPhoneNumberRepository
) {
    fun EmptyUser.setPhoneNumber(phoneNumber: PhoneNumber) {
        userPhoneNumberRepository.add(userId, phoneNumber)
    }
}
