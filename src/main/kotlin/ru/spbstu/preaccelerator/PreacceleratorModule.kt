package ru.spbstu.preaccelerator

import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule
import ru.spbstu.preaccelerator.data.createAppDatabase
import ru.spbstu.preaccelerator.data.createDataSource
import ru.spbstu.preaccelerator.data.readDatabaseCredentials
import ru.spbstu.preaccelerator.telegram.createModuleConfig
import ru.spbstu.preaccelerator.telegram.createStateMachine
import ru.spbstu.preaccelerator.telegram.notifications.moduleDeadlineNotifications
import ru.spbstu.preaccelerator.telegram.resources.strings.NotificationStrings.ModuleDeadline
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
            // также можно пользоваться afterDeadline(1.days) send ...
        }
    }
}
