package ru.spbstu.preaccelerator.data.adapters

import app.cash.sqldelight.ColumnAdapter
import dev.inmo.tgbotapi.types.UserId

object UserIdAdapter : ColumnAdapter<UserId, Long> {
    override fun decode(databaseValue: Long): UserId {
        return UserId(databaseValue)
    }

    override fun encode(value: UserId): Long {
        return value.chatId
    }
}
