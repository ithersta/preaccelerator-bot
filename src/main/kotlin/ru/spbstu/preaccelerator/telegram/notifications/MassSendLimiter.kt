package ru.spbstu.preaccelerator.telegram.notifications

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.core.annotation.Single
import kotlin.time.Duration.Companion.seconds

@Single
class MassSendLimiter {
    private val mutex = Mutex()

    suspend fun wait() = mutex.withLock {
        delay(0.2.seconds)
    }
}
