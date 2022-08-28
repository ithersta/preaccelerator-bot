package ru.spbstu.preaccelerator.telegram

import com.ithersta.tgbotapi.fsm.builders.RoleFilterBuilder
import com.ithersta.tgbotapi.fsm.builders.StateFilterBuilder
import com.ithersta.tgbotapi.fsm.builders.StateMachineBuilder
import com.ithersta.tgbotapi.fsm.builders.stateMachine
import com.ithersta.tgbotapi.fsm.repository.StateRepository
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.types.UserId
import mu.KotlinLogging
import ru.spbstu.preaccelerator.domain.entities.user.Curator
import ru.spbstu.preaccelerator.domain.entities.user.Member
import ru.spbstu.preaccelerator.domain.entities.user.PreacceleratorUser
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.domain.usecases.GetUserUseCase
import ru.spbstu.preaccelerator.telegram.entities.state.DialogState
import ru.spbstu.preaccelerator.telegram.flows.*
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings
import java.time.OffsetDateTime

typealias StateMachineBuilder = StateMachineBuilder<DialogState, PreacceleratorUser, UserId>
typealias RoleFilterBuilder<U> = RoleFilterBuilder<DialogState, PreacceleratorUser, U, UserId>
typealias StateFilterBuilder<S, U> = StateFilterBuilder<DialogState, PreacceleratorUser, S, U, UserId>

private val logger = KotlinLogging.logger { }

fun createStateMachine(
    stateRepository: StateRepository<UserId, DialogState>,
    getUser: GetUserUseCase
) = stateMachine({ getUser(it, OffsetDateTime.now()) }, stateRepository) {
    onException { userId, throwable ->
        logger.info(throwable) { userId }
        sendTextMessage(userId, MessageStrings.Error.internal(throwable.message))
    }
    includeHelp()
    anyRole {
        anyState {
            cancelCommand()
            whoCommand()
            stateCommand()
        }
    }
    startFlow()
    role<Curator> {

    }
    role<Tracker> {

    }
    role<Member> {

    }
    fallback()
}
