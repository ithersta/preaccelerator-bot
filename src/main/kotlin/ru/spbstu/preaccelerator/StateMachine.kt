package ru.spbstu.preaccelerator

import com.ithersta.tgbotapi.fsm.builders.stateMachine
import com.ithersta.tgbotapi.fsm.repository.InMemoryStateRepositoryImpl
import com.ithersta.tgbotapi.fsm.repository.StateRepository
import dev.inmo.tgbotapi.types.UserId
import ru.spbstu.preaccelerator.entities.Role
import ru.spbstu.preaccelerator.entities.state.DialogState
import ru.spbstu.preaccelerator.entities.state.EmptyState
import ru.spbstu.preaccelerator.flows.cancelCommand
import ru.spbstu.preaccelerator.flows.fallback
import ru.spbstu.preaccelerator.flows.startFlow

fun createStateMachine(
    stateRepository: StateRepository<UserId, DialogState>
) = stateMachine<Role, DialogState>(
    getRole = { null },
    stateRepository = stateRepository
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
