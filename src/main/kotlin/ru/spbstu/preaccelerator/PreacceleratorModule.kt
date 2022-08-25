package ru.spbstu.preaccelerator

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.module
import org.koin.ksp.generated.module
import ru.spbstu.preaccelerator.data.createAppDatabase
import ru.spbstu.preaccelerator.data.createDataSource
import ru.spbstu.preaccelerator.telegram.createStateMachine

val preacceleratorModule = module(createdAtStart = true) {
    includes(ComponentScanModule().module)
    single { createDataSource() }
    single { createAppDatabase(get()) }
    single { createStateMachine(get(), get()) }
}

@Module
@ComponentScan
class ComponentScanModule
