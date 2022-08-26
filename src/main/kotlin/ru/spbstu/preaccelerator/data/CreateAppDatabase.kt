package ru.spbstu.preaccelerator.data

import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import ru.spbstu.preaccelerator.data.adapters.*
import javax.sql.DataSource

fun createAppDatabase(dataSource: DataSource): AppDatabase {
    val driver = dataSource.asJdbcDriver()
    return AppDatabase(
        driver = driver,
        UserDialogStateAdapter = UserDialogState.Adapter(
            stateAdapter = DialogStateAdapter,
            userIdAdapter = UserIdAdapter
        ),
        UserPhoneNumberAdapter = UserPhoneNumber.Adapter(
            userIdAdapter = UserIdAdapter,
            phoneNumberAdapter = PhoneNumberAdapter
        ),
        TeamAdapter = Team.Adapter(
            idAdapter = TeamIdAdapter,
            trackerIdAdapter = TrackerIdAdapter
        ),
        CuratorAdapter = Curator.Adapter(
            userIdAdapter = UserIdAdapter
        ),
        MemberAdapter = Member.Adapter(
            idAdapter = MemberIdAdapter,
            phoneNumberAdapter = PhoneNumberAdapter,
            teamIdAdapter = TeamIdAdapter
        ),
        TrackerAdapter = Tracker.Adapter(
            idAdapter = TrackerIdAdapter,
            phoneNumberAdapter = PhoneNumberAdapter
        )
    ).also {
        runCatching {
            AppDatabase.Schema.create(driver)
            AppDatabase.Schema.migrate(driver, 1, 2)
        }.onFailure {
            it.printStackTrace()
        }
    }
}
