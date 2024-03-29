package ru.spbstu.preaccelerator.telegram.flows.menus

import com.ithersta.tgbotapi.fsm.builders.RoleFilterBuilder
import com.ithersta.tgbotapi.menu.builders.menu

import dev.inmo.tgbotapi.types.UserId
import ru.spbstu.preaccelerator.domain.entities.user.PreacceleratorUser
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.telegram.entities.state.*
import ru.spbstu.preaccelerator.telegram.flows.tracker.sendStatisticsTracker
import ru.spbstu.preaccelerator.telegram.flows.tracker.addNewMeetingFlow
import ru.spbstu.preaccelerator.telegram.flows.tracker.downloadHomeworkFlow
import ru.spbstu.preaccelerator.telegram.flows.tracker.fillOutProtocolFlow
import ru.spbstu.preaccelerator.telegram.resources.strings.DescriptionStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings.Tracker.Meetings
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings.Tracker.Teams

val trackerMenu = menu<DialogState, PreacceleratorUser, Tracker>(MenuStrings.Tracker.Message, EmptyState) {
    submenu(MenuStrings.Tracker.SendInfo.Button, MenuStrings.Tracker.SendInfo.Message, MenuState.Tracker.SendInfo) {
        button(
            MenuStrings.Tracker.SendInfo.ToAll, SendInfoState.WaitingForMessage(Recipient.AllFromTracker),
            DescriptionStrings.sendInfo(DescriptionStrings.Roles.Users)
        )
        button(
            MenuStrings.Tracker.SendInfo.ToSelectTeams, SendInfoState.ChooseTeams(),
            DescriptionStrings.sendInfo(DescriptionStrings.Roles.Teams)
        )
        backButton(MenuStrings.Back)
    }
    submenu(Meetings.Button, Meetings.Message, MenuState.Tracker.Meetings) {
        button(
            Meetings.Schedule,
            NewMeetingState.WaitingForModuleNumber,
            DescriptionStrings.TrackerButtons.ScheduleMeeting
        )
        button(Meetings.SendProtocol, FillOutProtocolState.ChooseTeam, DescriptionStrings.TrackerButtons.SendProtocol)
        backButton(MenuStrings.Back)
    }
    submenu(Teams.Button, Teams.Message, MenuState.Tracker.Teams) {
        button(Teams.GetHomework, DownloadHomeworkState, DescriptionStrings.TrackerButtons.DownloadHomework)
        button(
            Teams.GetStats,
            DescriptionStrings.getStats(DescriptionStrings.Roles.Teams)
        ) { sendStatisticsTracker(it) }
        backButton(MenuStrings.Back)
    }
}

fun RoleFilterBuilder<DialogState, PreacceleratorUser, Tracker, UserId>.trackerMenu() {
    with(trackerMenu) { invoke() }
    addNewMeetingFlow()
    downloadHomeworkFlow()
    fillOutProtocolFlow()
}
