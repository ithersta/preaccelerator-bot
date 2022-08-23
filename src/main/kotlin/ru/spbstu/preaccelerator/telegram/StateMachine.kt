package ru.spbstu.preaccelerator.telegram

import com.ithersta.tgbotapi.fsm.builders.stateMachine
import com.ithersta.tgbotapi.fsm.repository.StateRepository
import dev.inmo.tgbotapi.types.UserId
import ru.spbstu.preaccelerator.domain.entities.Role
import ru.spbstu.preaccelerator.domain.entities.Role.*
import ru.spbstu.preaccelerator.telegram.entities.state.DialogState
import ru.spbstu.preaccelerator.telegram.flows.cancelCommand
import ru.spbstu.preaccelerator.telegram.flows.fallback
import ru.spbstu.preaccelerator.telegram.flows.startFlow
import ru.spbstu.preaccelerator.telegram.flows.stateCommand

fun createStateMachine(
    stateRepository: StateRepository<UserId, DialogState>
) = stateMachine<Role, DialogState>(
    getRole = { null }, stateRepository
) {
    includeHelp()
    withoutRole {
        startFlow()
    }
    role(Curator) {

    }
    role(Tracker) {

    }
    role(Member) {

    }
    anyRole {
        anyState {
            cancelCommand()
            stateCommand()
            fallback()
        }
    }
}
