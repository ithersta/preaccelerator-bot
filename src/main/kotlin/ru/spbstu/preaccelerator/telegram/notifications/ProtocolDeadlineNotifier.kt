package ru.spbstu.preaccelerator.telegram.notifications

import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single
import org.quartz.CronScheduleBuilder.dailyAtHourAndMinute
import org.quartz.JobBuilder
import org.quartz.JobExecutionContext
import org.quartz.Scheduler
import org.quartz.TriggerBuilder.newTrigger
import ru.spbstu.preaccelerator.domain.entities.module.Module
import ru.spbstu.preaccelerator.domain.usecases.GetUnfinishedTrackersUseCase
import ru.spbstu.preaccelerator.telegram.extensions.TrackerExt.userId
import ru.spbstu.preaccelerator.telegram.notifications.ProtocolDeadlineNotifier.Config
import java.time.LocalTime
import java.time.OffsetDateTime
import kotlin.time.Duration

@Single
class ProtocolDeadlineNotifier(
    private val massSendLimiter: MassSendLimiter,
    private val config: Config,
    private val getUnfinishedTrackers: GetUnfinishedTrackersUseCase
) {
    fun BehaviourContext.setupJobs(scheduler: Scheduler) {
        scheduler.setJobFactory { _, _ -> Job(this) }
        val job = JobBuilder.newJob(Job::class.java).build()
        val trigger = newTrigger()
            .withSchedule(dailyAtHourAndMinute(config.at.hour, config.at.minute))
            .startNow()
            .build()
        scheduler.scheduleJob(job, trigger)
    }

    inner class Job(private val behaviourContext: BehaviourContext) : org.quartz.Job {
        override fun execute(context: JobExecutionContext) {
            with(behaviourContext) {
                launch {
                    config.notifications.forEach { (duration, text) ->
                        val trackers = getUnfinishedTrackers(duration, OffsetDateTime.now())
                        trackers.forEach { (tracker, moduleNumber) ->
                            massSendLimiter.wait()
                            runCatching {
                                sendTextMessage(tracker.userId!!, text(moduleNumber))
                            }
                        }
                    }
                }
            }
        }
    }

    class Config(val at: LocalTime, val notifications: List<Pair<Duration, (Module.Number) -> String>>) {
        class Builder(private val at: LocalTime) {
            private val notifications = mutableListOf<Pair<Duration, (Module.Number) -> String>>()

            infix fun Duration.afterFirstMeetingSend(text: (Module.Number) -> String) {
                notifications.add(this to text)
            }

            fun build() = Config(at, notifications)
        }
    }
}

fun protocolDeadlineNotifications(at: LocalTime, block: Config.Builder.() -> Unit) =
    Config.Builder(at).apply(block).build()

