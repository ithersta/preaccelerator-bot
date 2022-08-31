package ru.spbstu.preaccelerator.telegram.flows.commands

import com.ithersta.tgbotapi.fsm.entities.triggers.onCommand
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.types.buttons.ReplyKeyboardRemove
import ru.spbstu.preaccelerator.domain.entities.user.*
import ru.spbstu.preaccelerator.telegram.StateFilterBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.DialogState
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState
import ru.spbstu.preaccelerator.telegram.extensions.MemberExt.team
import ru.spbstu.preaccelerator.telegram.resources.strings.HelpStrings
import ru.spbstu.preaccelerator.telegram.resources.strings.MessageStrings

fun StateFilterBuilder<DialogState, PreacceleratorUser>.whoCommand() {
    onCommand("who", HelpStrings.Who) {
        refreshCommands()
        val text = when (val user = user) {
            is EmptyUser -> MessageStrings.Who.Empty
            is Curator -> MessageStrings.Who.Curator
            is Member -> MessageStrings.Who.member(user.team)
            is Tracker -> MessageStrings.Who.tracker(user.id)
        }
        sendTextMessage(it.chat, text, replyMarkup = ReplyKeyboardRemove())
        setState(EmptyState)
    }
}
