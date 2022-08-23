package ru.spbstu.preaccelerator

import com.ithersta.tgbotapi.fsm.builders.stateMachine
import com.ithersta.tgbotapi.fsm.repository.InMemoryStateRepositoryImpl
import ru.spbstu.preaccelerator.entities.Role
import ru.spbstu.preaccelerator.entities.state.DialogState
import ru.spbstu.preaccelerator.entities.state.EmptyState
import ru.spbstu.preaccelerator.flows.cancelCommand
import ru.spbstu.preaccelerator.flows.fallback
import ru.spbstu.preaccelerator.flows.startFlow

val stateMachine = stateMachine<Role, DialogState>(
    getRole = { null },
    stateRepository = InMemoryStateRepositoryImpl(EmptyState)
) {
    includeHelp()
    withoutRole {
        startFlow()
    }
    anyRole {
        anyState {
            cancelCommand()
            fallback()
        }
    }
}
