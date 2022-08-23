package ru.spbstu.preaccelerator.repository

import com.ithersta.tgbotapi.fsm.repository.StateRepository
import dev.inmo.tgbotapi.types.UserId
import ru.spbstu.preaccelerator.data.AppDatabase
import ru.spbstu.preaccelerator.entities.state.DialogState
import ru.spbstu.preaccelerator.entities.state.EmptyState

class UserDialogStateRepository(
    private val appDatabase: AppDatabase
) : StateRepository<UserId, DialogState> {
    override fun get(key: UserId): DialogState {
        return appDatabase.userDialogStateQueries.get(key).executeAsOneOrNull()?.state ?: EmptyState
    }

    override fun set(key: UserId, state: DialogState) {
        appDatabase.userDialogStateQueries.set(key, state)
    }
}
