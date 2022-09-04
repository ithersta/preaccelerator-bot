package ru.spbstu.preaccelerator.telegram.parsers

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import ru.spbstu.preaccelerator.domain.entities.PhoneNumber
import java.io.InputStream

const val MEMBERS_SHEET_NAME = "Участники"
const val TEAMS_SHEET_NAME = "Команды"

object Xlsx {
    sealed interface Result<T> {
        class OK<T>(val value: T) : Result<T>
        class BadFormat<T>(val errors: List<TableErrors>) : Result<T>
        class InvalidFile<T> : Result<T>
    }

    class TableErrors(
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
                parsePhonesWithTeam(workbook.getSheet(MEMBERS_SHEET_NAME)) to
                        parsePhonesWithTeam(workbook.getSheet(TEAMS_SHEET_NAME))
            }
            val membersErrorRows = members.mapIndexedNotNull { index, pair -> (index + 2).takeIf { pair == null } }
            val teamsErrorRows = teams.mapIndexedNotNull { index, pair -> (index + 2).takeIf { pair == null } }
            val errors = listOf(
                TableErrors(MEMBERS_SHEET_NAME, membersErrorRows),
                TableErrors(TEAMS_SHEET_NAME, teamsErrorRows)
            ).filter { it.rows.isNotEmpty() }
            if (errors.isNotEmpty()) {
                Result.BadFormat(errors)
            } else {
                Result.OK(Users(members.requireNoNulls(), teams.requireNoNulls()))
            }
        }.getOrElse {
            Result.InvalidFile()
        }

    private fun parsePhonesWithTeam(sheet: XSSFSheet): List<Pair<PhoneNumber, String>?> {
        return sheet
            .map { it.getCell(0).getText() to it.getCell(1).getText() }
            .drop(1)
            .dropLastWhile { it.first?.isBlank() == true }
            .map {
                runCatching {
                    val phoneNumber = PhoneNumber.of(it.first!!.removePrefix("+"))!!
                    val teamName = it.second!!
                    phoneNumber to teamName
                }.getOrNull()
            }
    }

    private fun Cell.getText(): String? {
        return when (cellType) {
            CellType.NUMERIC -> numericCellValue.toLong().toString()
            CellType.STRING -> stringCellValue
            CellType.BLANK -> ""
            else -> null
        }
    }
}
