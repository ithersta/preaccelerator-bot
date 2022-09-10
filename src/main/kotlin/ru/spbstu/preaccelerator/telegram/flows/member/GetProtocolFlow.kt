package ru.spbstu.preaccelerator.telegram.flows.member

import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.types.message.MarkdownV2
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.user.Member
import ru.spbstu.preaccelerator.domain.repository.TeamRepository
import ru.spbstu.preaccelerator.telegram.StateMachineBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState
import ru.spbstu.preaccelerator.telegram.entities.state.GetProtocolState
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.GetProtocol.teamProtocol

fun StateMachineBuilder.getProtocolFlow() {
    val teamRepository: TeamRepository by inject()

    role<Member> {
        state<GetProtocolState> {
            onTransition {
                sendTextMessage(
                    it,
                    teamProtocol(teamRepository.get(user.teamId)),
                    parseMode = MarkdownV2
                )
                setState(EmptyState)
            }

        }
    }

}