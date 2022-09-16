package ru.spbstu.preaccelerator.telegram.entities.state

import com.ithersta.tgbotapi.pagination.PagerState
import kotlinx.serialization.Serializable
import ru.spbstu.preaccelerator.domain.entities.Team

object SendInfoState {
    @Serializable
    data class ChooseTeams(
        val teamIds: Set<Team.Id> = emptySet(),
        val pagerState: PagerState = PagerState()
    ) : DialogState

    @Serializable
    data class WaitingForMessage(
        val recipient: Recipient
    ) : DialogState

    @Serializable
    data class WaitingForConfirmation(
        val recipient: Recipient,
        val message: String
    ) : DialogState
}

@Serializable
sealed interface Recipient {
    @Serializable
    object All : Recipient

    @Serializable
    object AllFromTracker : Recipient

    @Serializable
    object Trackers : Recipient

    @Serializable
    data class Teams(val ids: Set<Team.Id>) : Recipient
}
