package ru.spbstu.preaccelerator.telegram

import com.ithersta.tgbotapi.fsm.builders.RoleFilterBuilder
import com.ithersta.tgbotapi.fsm.builders.StateFilterBuilder
import com.ithersta.tgbotapi.fsm.builders.StateMachineBuilder
import com.ithersta.tgbotapi.fsm.builders.stateMachine
import com.ithersta.tgbotapi.fsm.repository.StateRepository
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.types.UserId
import ru.spbstu.preaccelerator.domain.entities.user.*
import ru.spbstu.preaccelerator.telegram.entities.state.DialogState
import ru.spbstu.preaccelerator.telegram.flows.cancelCommand
import ru.spbstu.preaccelerator.telegram.flows.fallback
import ru.spbstu.preaccelerator.telegram.flows.startFlow
import ru.spbstu.preaccelerator.telegram.flows.stateCommand
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings

typealias StateMachineBuilder = StateMachineBuilder<DialogState, PreacceleratorUser, UserId>
typealias RoleFilterBuilder<U> = RoleFilterBuilder<DialogState, PreacceleratorUser, U, UserId>
typealias StateFilterBuilder<S, U> = StateFilterBuilder<DialogState, PreacceleratorUser, S, U, UserId>

fun createStateMachine(
    stateRepository: StateRepository<UserId, DialogState>
) = stateMachine<_, PreacceleratorUser>({ EmptyUser }, stateRepository) {
    onException { userId, throwable ->
        sendTextMessage(userId, MessageStrings.Error.internal(throwable.message))
    }
    includeHelp()
    startFlow()
    role<Curator> {

    }
    role<Tracker> {

    }
    role<Member> {

    }
    anyRole {
        anyState {
            cancelCommand()
            stateCommand()
            fallback()
        }
    }
}
