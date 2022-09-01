package ru.spbstu.preaccelerator

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.module
import org.koin.ksp.generated.module
import ru.spbstu.preaccelerator.data.createAppDatabase
import ru.spbstu.preaccelerator.data.createDataSource
import ru.spbstu.preaccelerator.telegram.createModuleConfig
import ru.spbstu.preaccelerator.telegram.createStateMachine
//import ru.spbstu.preaccelerator.telegram.stepConfig

val preacceleratorModule = module(createdAtStart = true) {
    includes(ComponentScanModule().module)
    single { createDataSource() }
    single { createAppDatabase(get()) }
    single { createStateMachine(get(), get()) }
    single { createModuleConfig() }
}

@Module
@ComponentScan
class ComponentScanModule
