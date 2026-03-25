package com.blogspot.e_kanivets.moneytracker.util

import com.blogspot.e_kanivets.moneytracker.MtApp
import com.blogspot.e_kanivets.moneytracker.controller.FormatController
import com.blogspot.e_kanivets.moneytracker.entity.RecordItem
import com.blogspot.e_kanivets.moneytracker.entity.data.Record
import javax.inject.Inject

class RecordItemsBuilder {

    @Inject
    lateinit var formatController: FormatController

    constructor() {
        MtApp.instance.appComponent.inject(this)
    }

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