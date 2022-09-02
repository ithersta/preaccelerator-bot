package ru.spbstu.preaccelerator.data

import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

fun createDataSource(credentials: DatabaseCredentials): DataSource {
    return HikariDataSource().apply {
        dataSourceClassName = "org.postgresql.ds.PGSimpleDataSource"
        addDataSourceProperty("databaseName", credentials.name)
        credentials.hostname?.let { addDataSourceProperty("serverName", it) }
        credentials.port?.let { addDataSourceProperty("portNumber", it) }
        credentials.user?.let { addDataSourceProperty("user", it) }
        credentials.password?.let { addDataSourceProperty("password", it) }
    }
}
