package ru.spbstu.preaccelerator.telegram.parsers

import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import ru.spbstu.preaccelerator.domain.entities.PhoneNumber
import java.io.InputStream

const val MEMBERS_SHEET_NAME = "Участники"
const val TEAMS_SHEET_NAME = "Команды"

object Xlsx {
    sealed interface Result<T> {
        class OK<T>(val value: T) : Result<T>
        class BadFormat<T>(val errors: List<SheetErrors>) : Result<T>
        class InvalidFile<T> : Result<T>
    }

    class SheetErrors(
        val name: String,
        val rows: List<Int>
    )

    class Users(
        val members: List<Pair<PhoneNumber, String>>,
        val teams: List<Pair<PhoneNumber, String>>
    )

    fun parseUsers(inputStream: InputStream): Result<Users> =
        runCatching {
            val (members, teams) = XSSFWorkbook(inputStream).use { workbook ->
                parsePhonesWithTeam(workbook, MEMBERS_SHEET_NAME) to parsePhonesWithTeam(workbook, TEAMS_SHEET_NAME)
            }
            val errors = listOf(members.second, teams.second).filter { it.rows.isNotEmpty() }
            if (errors.isNotEmpty()) {
                Result.BadFormat(errors)
            } else {
                Result.OK(Users(members.first.requireNoNulls(), teams.first.requireNoNulls()))
            }
        }.getOrElse {
            println(it)
            Result.InvalidFile()
        }

    private fun parsePhonesWithTeam(
        workbook: Workbook,
        sheetName: String
    ): Pair<List<Pair<PhoneNumber, String>?>, SheetErrors> {
        return workbook.getSheet(sheetName)
            .map { it.getCellText(0) to it.getCellText(1) }
            .drop(1)
            .dropLastWhile { it.first.isNullOrBlank() && it.second.isNullOrBlank() }
            .map {
                runCatching {
                    val phoneNumber = PhoneNumber.of(it.first!!.removePrefix("+"))!!
                    val teamName = it.second!!
                    phoneNumber to teamName
                }.getOrNull()
            }
            .let {
                it to SheetErrors(sheetName, it.mapIndexedNotNull { index, e -> (index + 2).takeIf { e == null } })
            }
    }

    private fun Row.getCellText(number: Int) = runCatching {
        val cell = getCell(number)
        when (cell.cellType) {
            CellType.NUMERIC -> cell.numericCellValue.toLong().toString()
            CellType.STRING -> cell.stringCellValue
            CellType.BLANK -> ""
            else -> null
        }
    }.getOrNull()
}
