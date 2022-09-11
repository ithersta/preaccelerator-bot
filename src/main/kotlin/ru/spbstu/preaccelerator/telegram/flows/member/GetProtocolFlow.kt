package ru.spbstu.preaccelerator.telegram.flows.member

import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import org.koin.core.component.inject
import ru.spbstu.preaccelerator.domain.entities.user.Member
import ru.spbstu.preaccelerator.domain.repository.ProtocolRepository
import ru.spbstu.preaccelerator.domain.repository.TeamRepository
import ru.spbstu.preaccelerator.telegram.StateMachineBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState
import ru.spbstu.preaccelerator.telegram.entities.state.GetProtocolState
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.GetProtocol.NoProtocol
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings.GetProtocol.teamProtocol

fun StateMachineBuilder.getProtocolFlow() {
    val teamRepository: TeamRepository by inject()
    val protocolRepository: ProtocolRepository by inject()

    role<Member> {
        state<GetProtocolState> {
            onTransition {
                val protocol = protocolRepository.get(user.teamId)
                val team = teamRepository.get(user.teamId)
                sendTextMessage(it, if (protocol != null) teamProtocol(team, protocol) else NoProtocol)
                setState(EmptyState)
            }
        }
    }
}
