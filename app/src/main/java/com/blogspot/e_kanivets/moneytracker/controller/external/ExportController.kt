package com.blogspot.e_kanivets.moneytracker.controller.external

import com.blogspot.e_kanivets.moneytracker.controller.data.CategoryController
import com.blogspot.e_kanivets.moneytracker.controller.data.RecordController
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper

/**
 * Controller class to encapsulate export logic.
 * Created on 6/28/16.
 *
 * @author Evgenii Kanivets
 */
class ExportController(
    private val recordController: RecordController,
    private val categoryController: CategoryController
) {
    fun getRecordsForExport(fromDate: Long, toDate: Long): List<String> {
        val result = mutableListOf<String>()

        /* First of all add a header */
        val sb = StringBuilder()
        sb.append(Head.TIME).append(Head.DELIMITER)
        sb.append(Head.ACCOUNT_ID).append(Head.DELIMITER)
        sb.append(Head.TITLE).append(Head.DELIMITER)
        sb.append(Head.CATEGORY).append(Head.DELIMITER)
        sb.append(Head.NOTES).append(Head.DELIMITER)
        sb.append(Head.PRICE).append(Head.DELIMITER)
        sb.append(Head.CURRENCY)

        result.add(sb.toString())

        val condition = "${DbHelper.TIME_COLUMN} BETWEEN ? AND ?"
        val args = arrayOf<String?>(fromDate.toString(), toDate.toString())

        val recordList = recordController.readWithCondition(condition, args)

        for (record in recordList) {
            val row = StringBuilder()
            row.append(record.time).append(Head.DELIMITER)

            val accountId = record.account?.id ?: -1L
            row.append(accountId).append(Head.DELIMITER)
            row.append(record.title).append(Head.DELIMITER)

            val category = if (record.category != null)
                categoryController.read(record.category.id)
            else null

            row.append(if (category == null) "NONE" else category.name).append(Head.DELIMITER)
            row.append(record.notes).append(Head.DELIMITER)
            row.append(
                if (record.type == 0) record.fullPrice else -record.fullPrice
            ).append(Head.DELIMITER)
            row.append(record.currency)

            result.add(row.toString())
        }

        return result
    }
}
