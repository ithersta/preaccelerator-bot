package ru.spbstu.preaccelerator

import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule
import ru.spbstu.preaccelerator.data.createAppDatabase
import ru.spbstu.preaccelerator.data.createDataSource
import ru.spbstu.preaccelerator.data.readDatabaseCredentials
import ru.spbstu.preaccelerator.domain.entities.module.ModuleConfig
import ru.spbstu.preaccelerator.telegram.createModuleConfig
import ru.spbstu.preaccelerator.telegram.createStateMachine
import ru.spbstu.preaccelerator.telegram.notifications.moduleDeadlineNotifications
import ru.spbstu.preaccelerator.telegram.notifications.protocolDeadlineNotifications
import ru.spbstu.preaccelerator.telegram.resources.strings.NotificationStrings.ModuleDeadline
import ru.spbstu.preaccelerator.telegram.resources.strings.NotificationStrings.ProtocolDeadline
import java.time.LocalTime
import java.time.ZoneId
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
            1.days untilDeadlineSend ModuleDeadline::inOneDay
            val lastDay = get<ModuleConfig>().fullDuration.inWholeDays
            for (i in 1..lastDay step 3) {
                i.days afterDeadlineSend ModuleDeadline::expired
            }
        }
    }
    single {
        protocolDeadlineNotifications(LocalTime.of(19, 0)) {
            5.days afterFirstMeetingSend ProtocolDeadline::inLessThanTwoDays
        }
    }
    single { ZoneId.of("Asia/Yekaterinburg") }
}
