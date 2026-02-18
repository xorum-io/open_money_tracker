package com.blogspot.e_kanivets.moneytracker.controller.external

/**
 * Interface for head titles in import/export features.
 * Created on 6/28/16.
 *
 * @author Evgenii Kanivets
 */
interface Head {
    companion object {
        const val TIME = "time"
        const val ACCOUNT_ID = "account_id"
        const val TITLE = "title"
        const val CATEGORY = "category"
        const val NOTES = "notes"
        const val PRICE = "price"
        const val CURRENCY = "currency"
        const val DELIMITER = ";"
        const val COLUMN_COUNT = 7
    }
}
