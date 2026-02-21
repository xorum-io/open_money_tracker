package com.blogspot.e_kanivets.moneytracker.controller.external

import com.blogspot.e_kanivets.moneytracker.controller.data.RecordController
import com.blogspot.e_kanivets.moneytracker.entity.data.Account
import com.blogspot.e_kanivets.moneytracker.entity.data.Category
import com.blogspot.e_kanivets.moneytracker.entity.data.Record

/**
 * Controller class to encapsulate import logic.
 * Created on 6/28/16.
 *
 * @author Evgenii Kanivets
 */
class ImportController(private val recordController: RecordController) {

    fun importRecordsFromCsv(csv: String?): List<Record> {
        val recordList = mutableListOf<Record>()
        if (csv == null) return recordList

        val lines = csv.split("\\r?\\n".toRegex())
        for (line in lines) {
            val words = line.split(Head.DELIMITER)
            if (words.size != Head.COLUMN_COUNT) continue

            val timeCol = words[0]
            val accountIdCol = words[1]
            val titleCol = words[2]
            val categoryCol = words[3]
            val notesCol = words[4]
            val priceCol = words[5]
            val currencyCol = words[6]

            try {
                val time = timeCol.toLong()
                val accountId = accountIdCol.toLong()
                val title = titleCol.trim()
                val categoryName = categoryCol.trim()
                val notes = notesCol.trim()
                val price = priceCol.toDouble()
                val currency = currencyCol.trim()

                if (currency.length != 3) continue
                if (categoryName.isEmpty()) continue

                val type = if (price < 0.0) Record.TYPE_EXPENSE else Record.TYPE_INCOME

                val category = Category(categoryName)
                val account = Account(accountId, "MOCK", -1, currency, 0, -1.0, false, 0)

                val record = Record(time, type, title, category, notes, Math.abs(price), account, currency)
                val createdRecord = recordController.create(record)
                if (createdRecord != null) recordList.add(createdRecord)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return recordList
    }
}
