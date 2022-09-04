package ru.spbstu.preaccelerator.telegram.parsers

import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import ru.spbstu.preaccelerator.domain.entities.PhoneNumber
import java.io.InputStream

object Xlsx {
    sealed interface Result<T> {
        class OK<T>(val value: T) : Result<T>
        class BadFormat<T>(val errorRows: List<Int>) : Result<T>
        class InvalidFile<T> : Result<T>
    }

    fun parseXlsxTeams(inputStream: InputStream, sheetNumber: Int): Result<List<Pair<PhoneNumber, String>>> =
        runCatching {
            val trackers = XSSFWorkbook(inputStream).use { workbook ->
                workbook.getSheetAt(sheetNumber).map { row ->
                    try {
                        val cell = row.getCell(0)
                        val phoneNumber = when (cell.cellType) {
                            CellType.NUMERIC -> cell.numericCellValue.toLong().toString()
                            CellType.STRING -> cell.stringCellValue
                            else -> throw IllegalStateException()
                        }.removePrefix("+")
                        Pair<PhoneNumber?, String>(
                            PhoneNumber.of(phoneNumber),
                            row.getCell(1).stringCellValue
                        )
                    } catch (ignore: IllegalStateException) {
                        Pair<PhoneNumber?, String>(null, "")
                    }
                }.drop(1).dropLastWhile { it.first == null }
            }
            return if (trackers.any { it.first == null }) {
                Result.BadFormat(trackers.mapIndexedNotNull { index, pair ->
                    if (pair.first == null) {
                        index
                    } else {
                        null
                    }
                })
            } else {
                Result.OK(trackers.map { it.first!! to it.second })
            }
        }.getOrElse {
            Result.InvalidFile()
        }
}
