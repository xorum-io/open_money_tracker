package com.blogspot.e_kanivets.moneytracker.entity

sealed class RecordItem {

    data class Header(val date: String) : RecordItem()

    data class Record(val title: String, val categoryName: String, val notes: String, val fullPrice: Double, val currency: String, val isIncome: Boolean) : RecordItem() {
        constructor(record: com.blogspot.e_kanivets.moneytracker.entity.data.Record) : this(record.title, record.category?.name?.toString().orEmpty(), record.notes, record.fullPrice, record.currency, record.isIncome)
    }
}