package ru.spbstu.preaccelerator.domain.usecases

import dev.inmo.tgbotapi.types.UserId
import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.domain.repository.CuratorRepository
import java.security.SecureRandom
import java.util.*

private const val TOKEN_LENGTH = 24

@Single
class CuratorTokenInteractor(
    private val curatorRepository: CuratorRepository
) {
    private val secureRandom = SecureRandom()
    private val base64Encoder = Base64.getUrlEncoder()
    private val tokens = mutableListOf<String>()

    fun generate(): String {
        val randomBytes = ByteArray(TOKEN_LENGTH).also { secureRandom.nextBytes(it) }
        return base64Encoder.encodeToString(randomBytes).also {
            tokens.add(it)
        }
    }

    fun use(token: String, userId: UserId): Boolean {
        val foundMatch = tokens.removeIf { it == token }
        if (!foundMatch) {
            return false
        }
        curatorRepository.add(userId)
        return true
    }
}
