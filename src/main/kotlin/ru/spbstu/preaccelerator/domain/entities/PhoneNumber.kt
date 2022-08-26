package ru.spbstu.preaccelerator.domain.entities

@JvmInline
value class PhoneNumber private constructor(val value: String) {
    companion object {
        fun of(value: String): PhoneNumber? {
            return if (value.matches(Regex("\\d{2,15}"))) {
                PhoneNumber(value)
            } else {
                null
            }
        }
    }

    override fun toString(): String {
        return "+$value"
    }
}
