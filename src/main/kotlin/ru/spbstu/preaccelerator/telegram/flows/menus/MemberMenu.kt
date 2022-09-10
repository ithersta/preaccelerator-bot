package ru.spbstu.preaccelerator.telegram.flows.menus

import com.ithersta.tgbotapi.fsm.builders.RoleFilterBuilder
import com.ithersta.tgbotapi.menu.builders.menu
import dev.inmo.tgbotapi.types.UserId
import ru.spbstu.preaccelerator.domain.entities.user.Member
import ru.spbstu.preaccelerator.domain.entities.user.PreacceleratorUser
import ru.spbstu.preaccelerator.telegram.entities.state.ChooseModule
import ru.spbstu.preaccelerator.telegram.entities.state.DialogState
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState
import ru.spbstu.preaccelerator.telegram.entities.state.NotImplementedState
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings

val memberMenu = menu<DialogState, PreacceleratorUser, Member>(MenuStrings.Member.Message, EmptyState) {
    button(MenuStrings.Member.SelectModule, ChooseModule)
    button(MenuStrings.Member.GetProtocol, NotImplementedState)

}

fun RoleFilterBuilder<DialogState, PreacceleratorUser, Member, UserId>.memberMenu() {
    with(memberMenu) { invoke() }
}