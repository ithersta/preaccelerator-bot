package ru.spbstu.preaccelerator.telegram.extensions

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.PhoneNumber
import ru.spbstu.preaccelerator.domain.entities.user.EmptyUser
import ru.spbstu.preaccelerator.domain.usecases.actions.EmptyUserActions

object EmptyUserExt : KoinComponent {
    private val emptyUserActions: EmptyUserActions by inject()

    fun EmptyUser.setPhoneNumber(phoneNumber: PhoneNumber) = with(emptyUserActions) {
        setPhoneNumber(phoneNumber)
    }
}
