package ru.spbstu.preaccelerator.telegram.flows.menus

import com.ithersta.tgbotapi.fsm.StatefulContext
import com.ithersta.tgbotapi.fsm.builders.RoleFilterBuilder
import com.ithersta.tgbotapi.menu.menu
import dev.inmo.tgbotapi.extensions.api.bot.getMe
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.formatting.makeDeepLink
import dev.inmo.tgbotapi.types.UserId
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.content.TextContent
import ru.spbstu.preaccelerator.domain.entities.user.Curator
import ru.spbstu.preaccelerator.domain.entities.user.PreacceleratorUser
import ru.spbstu.preaccelerator.telegram.entities.state.DialogState
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState
import ru.spbstu.preaccelerator.telegram.entities.state.MenuState
import ru.spbstu.preaccelerator.telegram.entities.state.NotImplementedState
import ru.spbstu.preaccelerator.telegram.extensions.CuratorExt.generateCuratorToken
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings.Curator.AddUsers
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings.Curator.GetStats
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings.Curator.SendInfo
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings

fun RoleFilterBuilder<DialogState, PreacceleratorUser, Curator, UserId>.curatorMenu() {
    menu(MenuStrings.Curator.Message, EmptyState) {
        submenu(SendInfo.Button, SendInfo.Message, MenuState.Curator.SendInfo) {
            button(SendInfo.ToAll, NotImplementedState)
            button(SendInfo.ToTrackers, NotImplementedState)
            button(SendInfo.ToSelectTeams, NotImplementedState)
            backButton(MenuStrings.Back)
        }
        submenu(GetStats.Button, GetStats.Message, MenuState.Curator.GetStats) {
            button(GetStats.Teams, NotImplementedState)
            button(GetStats.Trackers, NotImplementedState)
            backButton(MenuStrings.Back)
        }
        submenu(AddUsers.Button, AddUsers.Message, MenuState.Curator.AddUsers) {
            button(AddUsers.MembersAndTrackers, NotImplementedState)
            button(AddUsers.Curator) { handleAddCurator(it) }
            backButton(MenuStrings.Back)
        }
        button(MenuStrings.Curator.GetProtocols, NotImplementedState)
    }
}

private suspend fun StatefulContext<DialogState, PreacceleratorUser, *, Curator>.handleAddCurator(message: CommonMessage<TextContent>) {
    val deepLink = makeDeepLink(getMe().username, user.generateCuratorToken())
    sendTextMessage(message.chat, MessageStrings.Curator.addCuratorDeepLink(deepLink))
}
