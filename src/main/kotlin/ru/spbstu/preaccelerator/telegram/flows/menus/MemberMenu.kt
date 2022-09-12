package ru.spbstu.preaccelerator.telegram.flows.menus

import com.ithersta.tgbotapi.fsm.builders.RoleFilterBuilder
import com.ithersta.tgbotapi.menu.builders.menu
import dev.inmo.tgbotapi.types.UserId
import ru.spbstu.preaccelerator.domain.entities.user.Member
import ru.spbstu.preaccelerator.domain.entities.user.PreacceleratorUser
import ru.spbstu.preaccelerator.telegram.entities.state.ChooseModuleState
import ru.spbstu.preaccelerator.telegram.entities.state.DialogState
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState
import ru.spbstu.preaccelerator.telegram.entities.state.GetProtocolState
import ru.spbstu.preaccelerator.telegram.flows.member.doModuleFlow
import ru.spbstu.preaccelerator.telegram.flows.member.getProtocolFlow
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings

val memberMenu = menu<DialogState, PreacceleratorUser, Member>(MenuStrings.Member.Message, EmptyState) {
    button(MenuStrings.Member.SelectModule, ChooseModuleState)
    button(MenuStrings.Member.GetProtocol, GetProtocolState)
}

fun RoleFilterBuilder<DialogState, PreacceleratorUser, Member, UserId>.memberMenu() {
    with(memberMenu) { invoke() }
    getProtocolFlow()
    doModuleFlow()
}
