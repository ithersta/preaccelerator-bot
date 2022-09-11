package ru.spbstu.preaccelerator.telegram.entities.state

import dev.inmo.tgbotapi.types.MessageIdentifier
import kotlinx.serialization.Serializable

@Serializable
data class SendInfoState(
    val typeMassMess: TypeMassMess,
    val startSending: Boolean,
    val mutableSetTeamId: MutableSet<Long>? = null,
    val listNamesTeams: List<Pair<Long, String>>? = null,
    val infoMess: String = "",
    val messageIdentifier: MessageIdentifier? = null
) : DialogState

enum class TypeMassMess {
    AllFromCurator, TrackersFromCurator, TeamsFromCuratorAndTacker, AllFromTracker
}