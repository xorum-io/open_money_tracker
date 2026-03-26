package com.blogspot.e_kanivets.moneytracker.util

import com.blogspot.e_kanivets.moneytracker.controller.FormatController
import com.blogspot.e_kanivets.moneytracker.entity.RecordItem
import com.blogspot.e_kanivets.moneytracker.entity.data.Record
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RecordItemsBuilder : KoinComponent {

    private val formatController: FormatController by inject()

    fun getRecordItems(recordList: List<Record>): List<RecordItem> {
        val recordItems: MutableList<RecordItem> = mutableListOf()

        var lastDate: String? = null
        for (record in recordList) {
            if (formatController.formatDateToString(record.time) != lastDate) {
                lastDate = formatController.formatDateToString(record.time)
                recordItems.add(RecordItem.Header(lastDate))
            }

            recordItems.add(RecordItem.Record(record))
        }

        return recordItems
    }

}