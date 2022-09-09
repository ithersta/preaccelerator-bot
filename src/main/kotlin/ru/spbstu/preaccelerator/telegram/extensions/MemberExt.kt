package ru.spbstu.preaccelerator.telegram.extensions

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.user.Member
import ru.spbstu.preaccelerator.domain.usecases.actions.MemberActions

object MemberExt : KoinComponent {
    private val actions: MemberActions by inject()

    val Member.team get() = with(actions) { team }
    val Member.userId get() = with(actions) { userId }
}
