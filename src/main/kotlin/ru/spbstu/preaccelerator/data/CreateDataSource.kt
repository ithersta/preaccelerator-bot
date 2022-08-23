package ru.spbstu.preaccelerator.data

import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

fun createDataSource(): DataSource {
    return HikariDataSource().apply {
        dataSourceClassName = "org.postgresql.ds.PGSimpleDataSource"
        addDataSourceProperty("databaseName", System.getenv("DATABASE_NAME"))
    }
}
