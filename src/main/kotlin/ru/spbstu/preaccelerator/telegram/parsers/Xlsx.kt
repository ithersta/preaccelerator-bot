package ru.spbstu.preaccelerator.telegram.parsers

import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.koin.core.context.GlobalContext
import ru.spbstu.preaccelerator.domain.entities.Homework
import ru.spbstu.preaccelerator.domain.entities.PhoneNumber
import ru.spbstu.preaccelerator.domain.entities.Team
import ru.spbstu.preaccelerator.domain.entities.module.ModuleConfig
import ru.spbstu.preaccelerator.telegram.resources.strings.SpreadsheetStrings
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.lang.RuntimeException

private val moduleConfig: ModuleConfig by GlobalContext.get().inject()

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

    fun createStatisticsSpreadsheet(teams: List<Team>, homeworks: List<Homework>): ByteArray {
        if (teams.isEmpty()) {
            throw RuntimeException("list of teams is empty")
        }
        val workbook = XSSFWorkbook()
        workbook.createSheet().apply {
            val style = workbook.createCellStyle().apply {
                setFont(workbook.createFont())
                alignment = HorizontalAlignment.CENTER
                verticalAlignment = VerticalAlignment.CENTER
                wrapText = false
            }
            createRow(0).apply {
                createCell(0).apply {
                    setCellValue(SpreadsheetStrings.StatisticsTable.TeamName)
                    cellStyle = style
                }
                addMergedRegion(CellRangeAddress(0, 2, 0, 0))
                createCell(1).apply {
                    setCellValue(SpreadsheetStrings.StatisticsTable.DateOfCompletion)
                    cellStyle = style
                }
                addMergedRegion(CellRangeAddress(0, 0, 1, moduleConfig.tasks.size))
            }
            var allTasks = 1
            createRow(1).apply {
                moduleConfig.modules.forEach { (number, module) ->
                    createCell(allTasks).apply {
                        setCellValue(SpreadsheetStrings.StatisticsTable.moduleString(number))
                        cellStyle = style
                    }
                    if (module.tasks.size > 1) {
                        addMergedRegion(CellRangeAddress(1, 1, allTasks, allTasks + module.tasks.size - 1))
                    }
                    allTasks += module.tasks.size
                }
            }
            createRow(2).apply {
                for (i in 1 until allTasks) {
                    createCell(i).apply {
                        setCellValue("№$i")
                        cellStyle = style
                    }
                }
            }
            teams.forEachIndexed { index, team ->
                createRow(index + 3).apply {
                    createCell(0).apply {
                        setCellValue(team.name)
                        cellStyle = style
                    }
                    homeworks.filter { homework ->
                        homework.teamId == team.id
                    }.sortedBy { it.taskNumber.value }.forEachIndexed { index, homework ->
                        createCell(index + 1).apply {
                            setCellValue(homework.timestamp.format(SpreadsheetStrings.StatisticsTable.OffsetDateTimeFormatter))
                            cellStyle = style
                        }
                    }
                }
            }
            createRow(3 + teams.size).apply {
                createCell(0).apply {
                    setCellValue(SpreadsheetStrings.StatisticsTable.TotalCompleted)
                    cellStyle = style
                }
                for (i in 1 until allTasks) {
                    createCell(i).apply {
                        setCellValue(homeworks.count { it.taskNumber.value == i }.toString())
                        cellStyle = style
                    }
                }
            }
            repeat(allTasks) {
                autoSizeColumn(it, true)
            }
        }
        return ByteArrayOutputStream().apply {
            workbook.write(this)
            workbook.close()
        }.toByteArray()
    }
}
