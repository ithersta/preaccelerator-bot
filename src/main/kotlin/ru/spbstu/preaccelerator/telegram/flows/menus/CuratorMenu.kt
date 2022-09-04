package ru.spbstu.preaccelerator.telegram.flows.menus

import com.ithersta.tgbotapi.fsm.builders.RoleFilterBuilder
import com.ithersta.tgbotapi.menu.menu
import dev.inmo.tgbotapi.types.UserId
import ru.spbstu.preaccelerator.domain.entities.user.Curator
import ru.spbstu.preaccelerator.domain.entities.user.PreacceleratorUser
import ru.spbstu.preaccelerator.telegram.entities.state.*
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings.Curator.AddUsers
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings.Curator.GetStats
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings.Curator.SendInfo

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
            button(AddUsers.MembersAndTrackers, LoadListOfUsersState.WaitingForDocument)
            button(AddUsers.Curator, NotImplementedState)
            backButton(MenuStrings.Back)
        }
        button(MenuStrings.Curator.GetProtocols, NotImplementedState)
    }
}
