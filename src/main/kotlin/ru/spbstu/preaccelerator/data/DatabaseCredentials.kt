package ru.spbstu.preaccelerator.data

class DatabaseCredentials(
    val name: String,
    val hostname: String?,
    val port: Int?,
    val user: String?,
    val password: String?
)

fun readDatabaseCredentials(): DatabaseCredentials {
    val environment = System.getenv()
    return DatabaseCredentials(
        name = environment.getValue("DATABASE_NAME"),
        hostname = environment["DATABASE_SERVER"],
        port = environment["DATABASE_PORT"]?.toInt(),
        user = environment["DATABASE_USER"],
        password = environment["DATABASE_PASSWORD"]
    )
}
