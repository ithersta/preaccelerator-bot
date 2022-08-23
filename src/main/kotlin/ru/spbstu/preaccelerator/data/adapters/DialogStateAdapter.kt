package ru.spbstu.preaccelerator.data.adapters

import app.cash.sqldelight.ColumnAdapter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import ru.spbstu.preaccelerator.telegram.entities.state.DialogState
import ru.spbstu.preaccelerator.telegram.entities.state.EmptyState

@OptIn(ExperimentalSerializationApi::class)
object DialogStateAdapter : ColumnAdapter<DialogState, ByteArray> {
    override fun decode(databaseValue: ByteArray): DialogState {
        return runCatching {
            Cbor.decodeFromByteArray<DialogState>(databaseValue)
        }.getOrDefault(EmptyState)
    }

    override fun encode(value: DialogState): ByteArray {
        return Cbor.encodeToByteArray(value)
    }
}
