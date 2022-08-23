package ru.spbstu.preaccelerator.data

import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import ru.spbstu.preaccelerator.data.adapters.DialogStateAdapter
import ru.spbstu.preaccelerator.data.adapters.UserIdAdapter
import javax.sql.DataSource

fun createAppDatabase(dataSource: DataSource): AppDatabase {
    val driver = dataSource.asJdbcDriver()
    return AppDatabase(driver,
    UserDialogState.Adapter(
        stateAdapter = DialogStateAdapter,
        userIdAdapter = UserIdAdapter
    )).also {
        runCatching {
            AppDatabase.Schema.create(driver)
        }
    }
}
