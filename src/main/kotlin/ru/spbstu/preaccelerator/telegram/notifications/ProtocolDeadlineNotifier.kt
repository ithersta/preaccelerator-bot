package ru.spbstu.preaccelerator.telegram.notifications

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.telegram.notifications.ProtocolDeadlineNotifier.Config
import java.time.OffsetTime
import kotlin.time.Duration

@Single
class ProtocolDeadlineNotifier(
    private val massSendLimiter: MassSendLimiter
) {
    class Config(val afterDays: Long, val at: OffsetTime) {
        class Builder {
            infix fun Duration.afterMeeting() = Partial(inWholeDays)
            infix fun Partial.at(at: OffsetTime) = Config(afterDays, at)

            class Partial(val afterDays: Long)
        }
    }
}

fun protocolDeadlineNotifications(block: Config.Builder.() -> Config) =
    Config.Builder().run(block)

