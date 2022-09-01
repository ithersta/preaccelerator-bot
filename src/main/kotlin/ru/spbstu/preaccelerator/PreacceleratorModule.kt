package ru.spbstu.preaccelerator

import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule
import ru.spbstu.preaccelerator.data.createAppDatabase
import ru.spbstu.preaccelerator.data.createDataSource
import ru.spbstu.preaccelerator.data.readDatabaseCredentials
import ru.spbstu.preaccelerator.telegram.createStateMachine
import ru.spbstu.preaccelerator.telegram.stepConfig

val preacceleratorModule = module(createdAtStart = true) {
    includes(defaultModule)
    single { readDatabaseCredentials() }
    single { createDataSource(get()) }
    single { createAppDatabase(get()) }
    single { createStateMachine(get(), get()) }
    single { stepConfig }
}
