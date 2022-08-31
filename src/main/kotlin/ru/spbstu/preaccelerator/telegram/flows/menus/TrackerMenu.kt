package ru.spbstu.preaccelerator.telegram.flows.menus

import com.ithersta.tgbotapi.fsm.builders.RoleFilterBuilder
import com.ithersta.tgbotapi.menu.menu
import dev.inmo.tgbotapi.types.UserId
import ru.spbstu.preaccelerator.domain.entities.user.PreacceleratorUser
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.telegram.entities.state.DialogState
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState
import ru.spbstu.preaccelerator.telegram.entities.state.MenuState
import ru.spbstu.preaccelerator.telegram.entities.state.NotImplementedState
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings.Tracker.Meetings
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings.Tracker.Teams

fun RoleFilterBuilder<DialogState, PreacceleratorUser, Tracker, UserId>.trackerMenu() {
    menu(MenuStrings.Tracker.Message, EmptyState) {
        submenu(Meetings.Button, Meetings.Message, MenuState.Tracker.Meetings) {
            button(Meetings.Schedule, NotImplementedState)
            button(Meetings.SendProtocol, NotImplementedState)
            backButton(MenuStrings.Back)
        }
        submenu(Teams.Button, Teams.Message, MenuState.Tracker.Teams) {
            button(Teams.GetHomework, NotImplementedState)
            button(Teams.GetStats, NotImplementedState)
            backButton(MenuStrings.Back)
        }
    }
}