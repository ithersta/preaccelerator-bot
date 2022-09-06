package ru.spbstu.preaccelerator.telegram.resources.strings

fun pluralize(quantity: Int, one: String, few: String, many: String = few) = when {
    quantity % 10 == 1 && quantity % 100 != 11 -> one
    quantity % 10 in 2..4 && quantity % 100 !in 12..14 -> few
    else -> many
}
