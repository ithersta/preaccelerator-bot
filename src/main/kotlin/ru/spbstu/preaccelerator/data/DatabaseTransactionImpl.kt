package ru.spbstu.preaccelerator.domain

import org.koin.core.annotation.Single
import ru.spbstu.preaccelerator.data.AppDatabase

@Single
class DatabaseTransactionImpl(private val appDatabase: AppDatabase) : DatabaseTransaction {
    override fun invoke(body: () -> Unit) {
        appDatabase.transaction { body() }
    }
}

@Single
class DatabaseTransactionWithResultImpl(private val appDatabase: AppDatabase) : DatabaseTransactionWithResult {
    override fun <R> invoke(body: () -> R): R {
        return appDatabase.transactionWithResult { body() }
    }
}
