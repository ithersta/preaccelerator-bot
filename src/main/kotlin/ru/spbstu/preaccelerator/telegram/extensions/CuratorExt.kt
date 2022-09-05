package ru.spbstu.preaccelerator.telegram.extensions

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.user.Curator
import ru.spbstu.preaccelerator.domain.usecases.actions.CuratorActions

object CuratorExt : KoinComponent {
    private val actions: CuratorActions by inject()

    fun Curator.generateCuratorToken() = with(actions) { generateCuratorToken() }
}
