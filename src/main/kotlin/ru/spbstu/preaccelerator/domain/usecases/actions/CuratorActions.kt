package ru.spbstu.preaccelerator.domain.usecases.actions

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.domain.entities.user.Curator
import ru.spbstu.preaccelerator.domain.usecases.CuratorTokenInteractor

@Single
class CuratorActions(
    private val curatorTokenInteractor: CuratorTokenInteractor
) {
    fun Curator.generateCuratorToken() = curatorTokenInteractor.generate()
}
