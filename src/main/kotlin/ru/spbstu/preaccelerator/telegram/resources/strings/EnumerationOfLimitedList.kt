package ru.spbstu.preaccelerator.telegram.resources.strings

fun <T> enumerationOfLimitedList(list: List<T>, limit: Int): String {
    return if (list.size <= limit) {
        list.map { it.toString() }.joinToString { it }
    } else {
        "${list.subList(0, limit).map { it.toString() }.joinToString { it }} и так далее."
    }
}