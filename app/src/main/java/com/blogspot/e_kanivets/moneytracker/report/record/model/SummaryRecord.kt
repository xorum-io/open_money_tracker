package com.blogspot.e_kanivets.moneytracker.report.record.model

import com.blogspot.e_kanivets.moneytracker.MtApp
import com.blogspot.e_kanivets.moneytracker.R

/**
 * Entity class.
 * Created on 2/25/16.
 *
 * @author Evgenii Kanivets
 */
class SummaryRecord(title: String, val currency: String, val amount: Double, recordsCount: Int) {
    val title: String = buildTitle(title, recordsCount)

    private fun buildTitle(title: String, recordsCount: Int): String {
        return if (recordsCount <= 1) title
        else MtApp.instance.resources.getString(R.string.title_summary_record, title, recordsCount)
    }
}
