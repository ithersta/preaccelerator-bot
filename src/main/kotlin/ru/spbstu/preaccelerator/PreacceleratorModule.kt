package ru.spbstu.preaccelerator

import com.ithersta.tgbotapi.fsm.repository.StateRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.spbstu.preaccelerator.data.createAppDatabase
import ru.spbstu.preaccelerator.data.createDataSource
import ru.spbstu.preaccelerator.telegram.repository.UserDialogStateRepository

val preacceleratorModule = module(createdAtStart = true) {
    single { createDataSource() }
    single { createAppDatabase(get()) }
    single { createStateMachine(get()) }
    singleOf(::UserDialogStateRepository) bind StateRepository::class
}
