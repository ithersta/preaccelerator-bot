package ru.spbstu.preaccelerator.telegram.parsers

import org.apache.logging.log4j.simple.SimpleLogger
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import ru.spbstu.preaccelerator.domain.entities.PhoneNumber
import java.io.InputStream
import java.util.logging.Logger

object Xlsx {
    sealed interface Result<T> {
        class OK<T>(val value: T) : Result<T>
        class BadFormat<T>(val errorRows: List<Int>) : Result<T>
        class InvalidFile<T> : Result<T>
    }
    fun parseXlsxTeams(inputStream: InputStream, sheetNumber: Int):
            Result<Map<PhoneNumber, String>> {
        try {
            val trackers = XSSFWorkbook(inputStream).use {
                it.getSheetAt(sheetNumber).map { row ->
                    try {
                        val cell = row.getCell(0)
                        val rawPhoneNumber = when(cell.cellType){
                            CellType.NUMERIC -> cell.numericCellValue.toLong().toString()
                            CellType.STRING -> cell.stringCellValue
                            else -> throw IllegalStateException()
                        }
                        System.out.println(rawPhoneNumber)
                        val phoneNumber = if (rawPhoneNumber[0] == '+') {
                            rawPhoneNumber.substring(1)
                        } else {
                            rawPhoneNumber
                        }
                        Pair<PhoneNumber?, String>(
                            PhoneNumber.of(phoneNumber),
                            row.getCell(1).stringCellValue
                        )
                    } catch (ignore: IllegalStateException) {
                        Pair<PhoneNumber?, String>(null, "")
                    }
                }.drop(1).dropLastWhile { it.first == null }
            }
            return if (trackers.any { p -> p.first == null }) {
                Result.BadFormat(trackers.mapIndexedNotNull { index, pair ->
                    if (pair.first == null) {
                        index
                    } else {
                        null
                    }
                })
            } else {
                Result.OK(trackers.map { Pair(it.first!!, it.second) }.toMap())
            }
        } catch (e: Exception) {
            return Result.InvalidFile()
        }
    }
}