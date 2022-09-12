package ru.spbstu.preaccelerator.data

import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import mu.KotlinLogging
import ru.spbstu.preaccelerator.data.adapters.*
import javax.sql.DataSource

private val logger = KotlinLogging.logger {}

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
        ),
        HomeworkAdapter = Homework.Adapter(
            teamIdAdapter = TeamIdAdapter,
            taskNumberAdapter = TaskNumberAdapter
        ),
        ProtocolStatusAdapter = ProtocolStatus.Adapter(
            teamIdAdapter = TeamIdAdapter,
            moduleNumberAdapter = ModuleNumberAdapter,
            value_Adapter = EnumColumnAdapter()
        ),
        MeetingAdapter = Meeting.Adapter(
            idAdapter = MeetingIdAdapter,
            teamIdAdapter = TeamIdAdapter,
            moduleNumberAdapter = ModuleNumberAdapter
        ),
        ProtocolAdapter = Protocol.Adapter(
            teamIdAdapter = TeamIdAdapter
        )
    ).also {
        runCatching {
            AppDatabase.Schema.create(driver)
            AppDatabase.Schema.migrate(driver, 1, 2)
        }.onFailure {
            logger.info("Database schema wasn't created")
        }
    }
}
