package ru.spbstu.preaccelerator.data

import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

fun createDataSource(): DataSource {
    val environment = System.getenv()
    return HikariDataSource().apply {
        dataSourceClassName = "org.postgresql.ds.PGSimpleDataSource"
        addDataSourceProperty("databaseName", environment.getValue("DATABASE_NAME"))
        environment["DATABASE_SERVER"]?.let { addDataSourceProperty("serverName", it) }
        environment["DATABASE_PORT"]?.let { addDataSourceProperty("portNumber", it.toInt()) }
        environment["DATABASE_USER"]?.let { addDataSourceProperty("user", it) }
        environment["DATABASE_PASSWORD"]?.let { addDataSourceProperty("password", it) }
    }
}
