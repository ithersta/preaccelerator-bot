package ru.spbstu.preaccelerator.telegram.notifications

import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single
import org.quartz.JobBuilder.newJob
import org.quartz.JobExecutionContext
import org.quartz.Trigger
import org.quartz.TriggerBuilder.newTrigger
import org.quartz.impl.StdSchedulerFactory
import ru.spbstu.preaccelerator.domain.entities.module.Module
import ru.spbstu.preaccelerator.domain.entities.module.ModuleConfig
import ru.spbstu.preaccelerator.domain.usecases.GetModuleDeadlinesUseCase
import ru.spbstu.preaccelerator.domain.usecases.GetUnfinishedMembersUseCase
import ru.spbstu.preaccelerator.telegram.extensions.MemberExt.userId
import java.time.OffsetDateTime
import java.util.*
import kotlin.time.Duration
import kotlin.time.toJavaDuration

private const val MODULE_NUMBER = "module_number"
private const val TEXT = "text"

@Single
class ModuleDeadlineNotifier(
    private val config: Config,
    private val moduleConfig: ModuleConfig,
    private val massSendLimiter: MassSendLimiter,
    private val getModuleDeadlines: GetModuleDeadlinesUseCase,
    private val getUnfinishedMembers: GetUnfinishedMembersUseCase
) {
    fun BehaviourContext.start() {
        val scheduler = StdSchedulerFactory.getDefaultScheduler()
        scheduler.setJobFactory { _, _ -> Job(this) }
        getModuleDeadlines().onEach { deadlines ->
            scheduler.clear()
            val job = newJob(Job::class.java).build()
            val triggers = deadlines.flatMap { (module, deadline) ->
                createTriggers(module, deadline)
            }
            scheduler.scheduleJob(job, triggers.toSet(), false)
        }.launchIn(this)
        scheduler.start()
    }

    private fun createTriggers(
        module: Module,
        deadline: OffsetDateTime
    ): List<Trigger> {
        val now = OffsetDateTime.now()
        return config.notifications.mapNotNull { (duration, text) ->
            val at = deadline + duration.toJavaDuration()
            if (at.isBefore(now)) return@mapNotNull null
            newTrigger()
                .usingJobData(TEXT, text(module.number))
                .usingJobData(MODULE_NUMBER, module.number.value)
                .startAt(Date.from(at.toInstant()))
                .build()
        }
    }

    inner class Job(private val behaviourContext: BehaviourContext) : org.quartz.Job {
        override fun execute(context: JobExecutionContext) {
            val moduleIndex = context.trigger.jobDataMap.getInt(MODULE_NUMBER)
            val module = moduleConfig.modules[moduleIndex]
            val text = context.trigger.jobDataMap.getString(TEXT)
            val members = getUnfinishedMembers(module)
            with(behaviourContext) {
                launch {
                    members.forEach { member ->
                        massSendLimiter.wait()
                        runCatching {
                            sendTextMessage(member.userId, text)
                        }
                    }
                }
            }
        }
    }

    class Config(
        val notifications: List<Pair<Duration, (Module.Number) -> String>>
    ) {
        class Builder {
            private val notifications = mutableListOf<Pair<Duration, (Module.Number) -> String>>()

            fun whenDeadlineIn(duration: Duration) = -duration
            fun afterDeadline(duration: Duration) = duration

            infix fun Duration.send(text: (Module.Number) -> String) {
                notifications.add(this to text)
            }

            fun build() = ModuleDeadlineNotifier.Config(notifications)
        }
    }
}

fun moduleDeadlineNotifications(block: ModuleDeadlineNotifier.Config.Builder.() -> Unit): ModuleDeadlineNotifier.Config {
    return ModuleDeadlineNotifier.Config.Builder().apply(block).build()
}
