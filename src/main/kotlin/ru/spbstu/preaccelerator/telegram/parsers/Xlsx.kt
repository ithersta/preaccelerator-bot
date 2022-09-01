package ru.spbstu.preaccelerator.telegram.parsers

import org.apache.logging.log4j.simple.SimpleLogger
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

    fun parseXlsxMembers(inputStream: InputStream):
            Result<Map<PhoneNumber, String>> {
        try {
            val users = XSSFWorkbook(inputStream)
                .getSheetAt(0).map { row ->
                try {
                    val tmp1 = row.getCell(0).stringCellValue
                    val tmp2 = if (tmp1[0] == '+'){
                        tmp1.substring(1)
                    } else{
                        tmp1
                    }
                    Pair<PhoneNumber?, String>(
                        PhoneNumber.of(tmp2),
                        row.getCell(1).stringCellValue
                    )
                } catch (ignore: IllegalStateException) {
                    Pair<PhoneNumber?, String>(null, "")
                }
            }.drop(1).dropLastWhile { it.first == null }
            return if (users.any { p -> p.first == null }) {
                Result.BadFormat(users.mapIndexedNotNull { index, pair ->
                    if (pair.first == null) {
                        index
                    } else {
                        null
                    }
                })
            } else {
                Result.OK(users.map { Pair(it.first!!, it.second) }.toMap())
            }
        } catch (e: Exception) {
            return Result.InvalidFile()
        }
    }

    fun parseXlsxTeams(inputStream: InputStream):
            Result<Map<PhoneNumber, String>> {
        try {
            val trackers = XSSFWorkbook(inputStream)
                .getSheetAt(1).map { row ->
                    try {
                        val tmp1 = row.getCell(1).stringCellValue
                        val tmp2 = if (tmp1[0] == '+'){
                            tmp1.substring(1)
                        } else{
                            tmp1
                        }
                        Pair<PhoneNumber?, String>(
                            PhoneNumber.of(tmp2),
                            row.getCell(0).stringCellValue
                        )
                    } catch (ignore: IllegalStateException) {
                        Pair<PhoneNumber?, String>(null, "")
                    }
                }.drop(1).dropLastWhile { it.first == null }
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