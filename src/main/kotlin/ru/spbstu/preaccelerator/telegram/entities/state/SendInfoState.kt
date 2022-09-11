package ru.spbstu.preaccelerator.telegram.entities.state

import dev.inmo.tgbotapi.types.MessageIdentifier
import dev.inmo.tgbotapi.types.UserId
import kotlinx.serialization.Serializable
import ru.spbstu.preaccelerator.domain.entities.Team

@Serializable
data class SendInfoState(
    val typeMassMess: TypeMassMess,
    val setTeamId: Set<Team.Id>? = null,
    val messageIdentifier: MessageIdentifier? = null
) : DialogState

@Serializable
data class InputMess(
    val listUserId: List<UserId>,
    val massMess: String
): DialogState

enum class TypeMassMess {
    AllFromCurator, TrackersFromCurator, TeamsFromCuratorAndTacker, AllFromTracker
}