package ru.spbstu.preaccelerator.telegram.flows

import com.ithersta.tgbotapi.fsm.entities.triggers.onText
import com.ithersta.tgbotapi.fsm.entities.triggers.onTransition
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.replyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.row
import dev.inmo.tgbotapi.extensions.utils.types.buttons.simpleButton
import ru.spbstu.preaccelerator.domain.entities.user.Tracker
import ru.spbstu.preaccelerator.telegram.StateMachineBuilder
import ru.spbstu.preaccelerator.telegram.entities.state.NewMeeting
import ru.spbstu.preaccelerator.telegram.resources.strings.MenuStrings

fun StateMachineBuilder.addNewMeeting(){
    role<Tracker> {
        state<NewMeeting>{
            onTransition {
                sendTextMessage(
                    it,
                    MenuStrings.Tracker.ScheduleMeetings.ChooseCommand,
                    replyMarkup = replyKeyboard(
                        resizeKeyboard=true,
                        oneTimeKeyboard = true
                    )
                    {
                        row {
                            simpleButton("Тут будут команды")
                        }
                    }

                )
            }
            onText{message ->}
        }
    }
}

