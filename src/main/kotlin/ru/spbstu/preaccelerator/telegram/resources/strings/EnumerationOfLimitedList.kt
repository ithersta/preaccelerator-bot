package ru.spbstu.preaccelerator.telegram.resources.strings

fun <T> enumerationOfLimitedList(list: List<T>, limit: Int) = buildString {
    append(list.take(limit).joinToString())
    if (list.size > limit) append("и так далее")
}
