package ru.spbstu.preaccelerator

import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule
import ru.spbstu.preaccelerator.data.createAppDatabase
import ru.spbstu.preaccelerator.data.createDataSource
import ru.spbstu.preaccelerator.data.readDatabaseCredentials
import ru.spbstu.preaccelerator.telegram.createModuleConfig
import ru.spbstu.preaccelerator.telegram.createStateMachine
import ru.spbstu.preaccelerator.telegram.notifications.moduleDeadlineNotifications
import ru.spbstu.preaccelerator.telegram.notifications.protocolDeadlineNotifications
import ru.spbstu.preaccelerator.telegram.resources.strings.NotificationStrings.ModuleDeadline
import java.time.OffsetTime
import kotlin.time.Duration.Companion.days

val preacceleratorModule = module(createdAtStart = true) {
    includes(defaultModule)
    single { readDatabaseCredentials() }
    single { createDataSource(get()) }
    single { createAppDatabase(get()) }
    single { createStateMachine(get(), get()) }
    single { createModuleConfig() }
    single {
        moduleDeadlineNotifications {
            whenDeadlineIn(1.days) send ModuleDeadline::inOneDay
            for (i in 1..56 step 3) {
                afterDeadline(i.days) send ModuleDeadline::expired
            }
        }
    }
    single {
        protocolDeadlineNotifications() {
            5.days afterMeetingAt OffsetTime.parse("19:00:00+03:00")
        }
    }
}
