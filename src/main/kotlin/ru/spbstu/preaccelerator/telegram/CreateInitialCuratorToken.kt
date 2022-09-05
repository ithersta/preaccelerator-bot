package ru.spbstu.preaccelerator.telegram

import dev.inmo.tgbotapi.bot.RequestsExecutor
import dev.inmo.tgbotapi.extensions.api.bot.getMe
import dev.inmo.tgbotapi.extensions.utils.formatting.makeDeepLink
import mu.KotlinLogging
import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.domain.repository.CuratorRepository
import ru.spbstu.preaccelerator.domain.usecases.CuratorTokenInteractor

private val logger = KotlinLogging.logger { }

@Single
class CreateInitialCuratorToken(
    private val curatorRepository: CuratorRepository,
    private val curatorTokenInteractor: CuratorTokenInteractor
) {
    suspend operator fun RequestsExecutor.invoke() {
        if (curatorRepository.isEmpty()) {
            val deepLink = makeDeepLink(getMe().username, curatorTokenInteractor.generate())
            logger.info("Initial curator deep link: $deepLink")
        }
    }
}
