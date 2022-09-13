package ru.spbstu.preaccelerator.telegram

import java.io.File

fun readToken(): String {
    return System.getenv()["TOKEN"] ?: readFromFile()
}

private fun readFromFile(): String {
    return File(System.getenv("TOKEN_FILE")).readText()
}
