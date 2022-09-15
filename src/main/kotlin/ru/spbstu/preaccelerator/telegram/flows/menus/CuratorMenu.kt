package ru.spbstu.preaccelerator.telegram.flows.menus

import com.ithersta.tgbotapi.fsm.StatefulContext
import com.ithersta.tgbotapi.fsm.builders.RoleFilterBuilder
import com.ithersta.tgbotapi.menu.builders.menu
import dev.inmo.tgbotapi.extensions.api.bot.getMe
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.formatting.makeDeepLink
import dev.inmo.tgbotapi.types.UserId
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.content.TextContent
import ru.spbstu.preaccelerator.domain.entities.user.Curator
import ru.spbstu.preaccelerator.domain.entities.user.PreacceleratorUser
import ru.spbstu.preaccelerator.telegram.entities.state.*
import ru.spbstu.preaccelerator.telegram.extensions.CuratorExt.generateCuratorToken
import ru.spbstu.preaccelerator.telegram.flows.curator.addUsersFlow
import ru.spbstu.preaccelerator.telegram.flows.curator.reviewProtocolsFlow
import ru.spbstu.preaccelerator.telegram.flows.curator.sendStatisticsCurator
import ru.spbstu.preaccelerator.telegram.resources.strings.DescriptionStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings.Curator.AddUsers
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings.Curator.GetStats
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings.Curator.SendInfo
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings

val curatorMenu = menu<DialogState, PreacceleratorUser, Curator>(MenuStrings.Curator.Message, EmptyState) {
    submenu(SendInfo.Button, SendInfo.Message, MenuState.Curator.SendInfo) {
        button(
            SendInfo.ToAll,
            SendInfoState.WaitingForMessage(Recipient.All),
            DescriptionStrings.sendInfo(DescriptionStrings.Roles.Users)
        )
        button(
            SendInfo.ToTrackers,
            SendInfoState.WaitingForMessage(Recipient.Trackers),
            DescriptionStrings.sendInfo(DescriptionStrings.Roles.Trackers)
        )
        button(
            SendInfo.ToSelectTeams,
            SendInfoState.ChooseTeams(),
            DescriptionStrings.sendInfo(DescriptionStrings.Roles.Teams)
        )
        backButton(MenuStrings.Back)
    }
    button(GetStats.Teams, DescriptionStrings.getStats(DescriptionStrings.Roles.Teams)){ sendStatisticsCurator()}
    submenu(AddUsers.Button, AddUsers.Message, MenuState.Curator.AddUsers) {
        button(
            AddUsers.MembersAndTrackers, AddUsersState.WaitingForDocument, DescriptionStrings.CuratorButtons.addUsers(
                listOf(DescriptionStrings.Roles.Members, DescriptionStrings.Roles.Trackers)
            )
        )
        button(
            AddUsers.Curator,
            DescriptionStrings.CuratorButtons.addUsers(listOf(DescriptionStrings.Roles.Curators))
        ) { handleAddCurator(it) }
        backButton(MenuStrings.Back)
    }
    button(
        MenuStrings.Curator.GetProtocols,
        ReviewProtocolsState.ChooseTeam,
        DescriptionStrings.CuratorButtons.GetProtocol
    )
}

fun RoleFilterBuilder<DialogState, PreacceleratorUser, Curator, UserId>.curatorMenu() {
    with(curatorMenu) { invoke() }
    addUsersFlow()
    reviewProtocolsFlow()
}

private suspend fun StatefulContext<DialogState, PreacceleratorUser, *, Curator>.handleAddCurator(message: CommonMessage<TextContent>) {
    val deepLink = makeDeepLink(getMe().username, user.generateCuratorToken())
    sendTextMessage(message.chat, MessageStrings.Curator.addCuratorDeepLink(deepLink))
}
