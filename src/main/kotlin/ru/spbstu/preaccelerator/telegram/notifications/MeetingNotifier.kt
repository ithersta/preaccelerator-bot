package ru.spbstu.preaccelerator.telegram.notifications

import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single
import org.quartz.JobBuilder.newJob
import org.quartz.JobExecutionContext
import org.quartz.Scheduler
import org.quartz.TriggerBuilder.newTrigger
import org.quartz.TriggerKey
import ru.spbstu.preaccelerator.domain.entities.Meeting
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.repository.MeetingRepository
import ru.spbstu.preaccelerator.domain.repository.TeamRepository
import ru.spbstu.preaccelerator.domain.repository.TrackerRepository
import ru.spbstu.preaccelerator.telegram.extensions.MemberExt.userId
import ru.spbstu.preaccelerator.telegram.extensions.TeamExt.members
import ru.spbstu.preaccelerator.telegram.extensions.TrackerExt.userId
import java.time.OffsetDateTime
import java.util.*
import kotlin.time.Duration
import kotlin.time.toJavaDuration

private const val TRIGGER_KEY_PREFIX = "meeting_notification"
private const val MEETING_ID = "meeting_id"

@Single
class MeetingNotifier(
    private val massSendLimiter: MassSendLimiter,
    private val meetingRepository: MeetingRepository,
    private val teamRepository: TeamRepository,
    private val trackerRepository: TrackerRepository,
    private val config: Config
) {
    fun BehaviourContext.setupJobs(scheduler: Scheduler) {
        scheduler.setJobFactory { _, _ -> Job(this) }
        val job = newJob(Job::class.java).storeDurably().build()
        scheduler.addJob(job, false)
        meetingRepository.getAllAsFlow().onEach { meetings ->
            val now = OffsetDateTime.now()
            meetings.forEach { meeting ->
                val triggerKey = TriggerKey("$TRIGGER_KEY_PREFIX ${meeting.id}")
                val at = meeting.timestamp - config.duration
                if (scheduler.checkExists(triggerKey).not() && at.isAfter(now)) {
                    val trigger = newTrigger()
                        .withIdentity(triggerKey)
                        .usingJobData(MEETING_ID, meeting.id.value)
                        .forJob(job.key)
                        .startAt(Date.from(at.toInstant()))
                        .build()
                    scheduler.scheduleJob(trigger)
                }
            }
        }.launchIn(this)
    }

    inner class Job(private val behaviourContext: BehaviourContext) : org.quartz.Job {
        override fun execute(context: JobExecutionContext) {
            val meetingId = Meeting.Id(context.trigger.jobDataMap.getLongValue(MEETING_ID))
            val meeting = meetingRepository.get(meetingId)
            val team = teamRepository.get(meeting.teamId)
            val tracker = trackerRepository.get(team.trackerId)
            with(behaviourContext) {
                launch {
                    team.members.forEach { member ->
                        massSendLimiter.wait()
                        runCatching {
                            sendTextMessage(member.userId!!, config.memberMessage(meeting))
                        }
                    }
                    massSendLimiter.wait()
                    runCatching {
                        sendTextMessage(tracker.userId!!, config.trackerMessage(meeting, team))
                    }
                }
            }
        }
    }

    class Config(
        val duration: java.time.Duration,
        val memberMessage: (Meeting) -> String,
        val trackerMessage: (Meeting, Team) -> String
    ) {
        class Builder {
            private var config: Config? = null

            infix fun Duration.beforeMeetingSendToTeam(text: (Meeting) -> String) =
                Partial(this.toJavaDuration(), text)

            infix fun Partial.sendToTracker(text: (Meeting, Team) -> String) {
                check(config == null)
                config = Config(duration, memberMessage, text)
            }

            fun build() = config!!

            class Partial(val duration: java.time.Duration, val memberMessage: (Meeting) -> String)
        }
    }
}

fun meetingNotifications(block: MeetingNotifier.Config.Builder.() -> Unit): MeetingNotifier.Config {
    return MeetingNotifier.Config.Builder().apply(block).build()
}
