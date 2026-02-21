package com.blogspot.e_kanivets.moneytracker.report.record.model

/**
 * Entity class.
 * Created on 2/25/16.
 *
 * @author Evgenii Kanivets
 */
class CategoryRecord(
    val title: String,
    val currency: String,
    val amount: Double
) {
    val summaryRecordList: MutableList<SummaryRecord> = mutableListOf()

    fun add(summaryRecord: SummaryRecord) {
        summaryRecordList.add(summaryRecord)
    }

    override fun equals(other: Any?): Boolean {
        if (other is CategoryRecord) {
            return title == other.title
                    && currency == other.currency
                    && amount == other.amount
                    && summaryRecordList == other.summaryRecordList
        }
        return false
    }
}
