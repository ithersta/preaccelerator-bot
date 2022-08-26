package ru.spbstu.preaccelerator.telegram.repository

import com.ithersta.tgbotapi.fsm.repository.StateRepository
import dev.inmo.tgbotapi.types.UserId
import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.data.AppDatabase
import ru.spbstu.preaccelerator.telegram.entities.state.DialogState
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState

@Single
class UserDialogStateRepository(
    private val appDatabase: AppDatabase
) : StateRepository<UserId, DialogState> {
    override fun get(key: UserId): DialogState {
        return appDatabase.userDialogStateQueries.get(key).executeAsOneOrNull() ?: EmptyState
    }

    override fun set(key: UserId, state: DialogState) {
        appDatabase.userDialogStateQueries.set(key, state)
    }
}
