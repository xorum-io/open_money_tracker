package com.blogspot.e_kanivets.moneytracker.repo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * DB Helper class.
 * Created on 29/08/14.
 *
 * @author Evgenii Kanivets
 */
class DbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        createDbVersion6(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.beginTransaction()

            db.execSQL("CREATE TABLE $TABLE_ACCOUNTS($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT,$CREATED_AT_COLUMN INTEGER,$TITLE_COLUMN TEXT,$CUR_SUM_COLUMN INTEGER,$CURRENCY_COLUMN TEXT );")
            db.execSQL("ALTER TABLE $TABLE_RECORDS ADD COLUMN $ACCOUNT_ID_COLUMN INTEGER;")
            db.execSQL("CREATE TABLE $TABLE_TRANSFERS($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT,$TIME_COLUMN INTEGER,$FROM_ACCOUNT_ID_COLUMN INTEGER,$TO_ACCOUNT_ID_COLUMN INTEGER,$FROM_AMOUNT_COLUMN INTEGER,$TO_AMOUNT_COLUMN INTEGER);")

            val id = insertDefaultAccount(db)

            val contentValues = ContentValues()
            contentValues.put(ACCOUNT_ID_COLUMN, id)
            db.update(TABLE_RECORDS, contentValues, null, null)

            db.setTransactionSuccessful()
            db.endTransaction()
        }

        if (oldVersion < 3) {
            db.beginTransaction()

            createRatesTable(db)
            db.execSQL("ALTER TABLE $TABLE_RECORDS ADD COLUMN $CURRENCY_COLUMN INTEGER;")

            db.setTransactionSuccessful()
            db.endTransaction()
        }

        if (oldVersion < 4) {
            db.beginTransaction()

            db.execSQL("ALTER TABLE $TABLE_RECORDS ADD COLUMN $DECIMALS_COLUMN INTEGER;")
            db.execSQL("ALTER TABLE $TABLE_ACCOUNTS ADD COLUMN $DECIMALS_COLUMN INTEGER;")
            db.execSQL("ALTER TABLE $TABLE_TRANSFERS ADD COLUMN $DECIMALS_FROM_COLUMN INTEGER;")
            db.execSQL("ALTER TABLE $TABLE_TRANSFERS ADD COLUMN $DECIMALS_TO_COLUMN INTEGER;")

            db.setTransactionSuccessful()
            db.endTransaction()
        }

        if (oldVersion < 5) {
            db.beginTransaction()

            db.execSQL("ALTER TABLE $TABLE_ACCOUNTS ADD COLUMN $GOAL_COLUMN REAL;")
            db.execSQL("ALTER TABLE $TABLE_ACCOUNTS ADD COLUMN $ARCHIVED_COLUMN INTEGER;")
            db.execSQL("ALTER TABLE $TABLE_ACCOUNTS ADD COLUMN $COLOR_COLUMN INTEGER;")

            db.setTransactionSuccessful()
            db.endTransaction()
        }

        if (oldVersion < 6) {
            db.beginTransaction()

            db.execSQL("ALTER TABLE $TABLE_RECORDS ADD COLUMN $NOTES_COLUMN TEXT;")

            val contentValues = ContentValues()
            contentValues.put(NOTES_COLUMN, "")
            db.update(TABLE_RECORDS, contentValues, null, null)

            db.setTransactionSuccessful()
            db.endTransaction()
        }
    }

    @Suppress("unused")
    private fun createDbVersion1(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_RECORDS($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT,$TIME_COLUMN INTEGER,$TYPE_COLUMN INTEGER,$TITLE_COLUMN TEXT,$CATEGORY_ID_COLUMN INTEGER,$PRICE_COLUMN INTEGER);")
        db.execSQL("CREATE TABLE $TABLE_CATEGORIES($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT,$NAME_COLUMN TEXT);")
    }

    @Suppress("unused")
    private fun createDbVersion2(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_RECORDS($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT,$TIME_COLUMN INTEGER,$TYPE_COLUMN INTEGER,$TITLE_COLUMN TEXT,$CATEGORY_ID_COLUMN INTEGER,$PRICE_COLUMN INTEGER,$ACCOUNT_ID_COLUMN INTEGER);")
        db.execSQL("CREATE TABLE $TABLE_CATEGORIES($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT,$NAME_COLUMN TEXT);")
        db.execSQL("CREATE TABLE $TABLE_ACCOUNTS($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT,$CREATED_AT_COLUMN INTEGER,$TITLE_COLUMN TEXT,$CUR_SUM_COLUMN INTEGER,$CURRENCY_COLUMN TEXT );")
        db.execSQL("CREATE TABLE $TABLE_TRANSFERS($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT,$TIME_COLUMN INTEGER,$FROM_ACCOUNT_ID_COLUMN INTEGER,$TO_ACCOUNT_ID_COLUMN INTEGER,$FROM_AMOUNT_COLUMN INTEGER,$TO_AMOUNT_COLUMN INTEGER);")
        insertDefaultAccount(db)
    }

    @Suppress("unused")
    private fun createDbVersion3(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_RECORDS($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT,$TIME_COLUMN INTEGER,$TYPE_COLUMN INTEGER,$TITLE_COLUMN TEXT,$CATEGORY_ID_COLUMN INTEGER,$PRICE_COLUMN INTEGER,$ACCOUNT_ID_COLUMN INTEGER,$CURRENCY_COLUMN TEXT);")
        db.execSQL("CREATE TABLE $TABLE_CATEGORIES($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT,$NAME_COLUMN TEXT);")
        db.execSQL("CREATE TABLE $TABLE_ACCOUNTS($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT,$CREATED_AT_COLUMN INTEGER,$TITLE_COLUMN TEXT,$CUR_SUM_COLUMN INTEGER,$CURRENCY_COLUMN TEXT );")
        db.execSQL("CREATE TABLE $TABLE_TRANSFERS($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT,$TIME_COLUMN INTEGER,$FROM_ACCOUNT_ID_COLUMN INTEGER,$TO_ACCOUNT_ID_COLUMN INTEGER,$FROM_AMOUNT_COLUMN INTEGER,$TO_AMOUNT_COLUMN INTEGER);")
        createRatesTable(db)
        insertDefaultAccount(db)
    }

    private fun createDbVersion4(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_RECORDS($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT,$TIME_COLUMN INTEGER,$TYPE_COLUMN INTEGER,$TITLE_COLUMN TEXT,$CATEGORY_ID_COLUMN INTEGER,$PRICE_COLUMN INTEGER,$ACCOUNT_ID_COLUMN INTEGER,$CURRENCY_COLUMN TEXT,$DECIMALS_COLUMN INTEGER);")
        db.execSQL("CREATE TABLE $TABLE_CATEGORIES($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT,$NAME_COLUMN TEXT);")
        db.execSQL("CREATE TABLE $TABLE_ACCOUNTS($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT,$CREATED_AT_COLUMN INTEGER,$TITLE_COLUMN TEXT,$CUR_SUM_COLUMN INTEGER,$CURRENCY_COLUMN TEXT,$DECIMALS_COLUMN INTEGER);")
        db.execSQL("CREATE TABLE $TABLE_TRANSFERS($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT,$TIME_COLUMN INTEGER,$FROM_ACCOUNT_ID_COLUMN INTEGER,$TO_ACCOUNT_ID_COLUMN INTEGER,$FROM_AMOUNT_COLUMN INTEGER,$TO_AMOUNT_COLUMN INTEGER,$DECIMALS_FROM_COLUMN INTEGER,$DECIMALS_TO_COLUMN INTEGER);")
        createRatesTable(db)
        insertDefaultAccount(db)
    }

    private fun createDbVersion5(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_RECORDS($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT,$TIME_COLUMN INTEGER,$TYPE_COLUMN INTEGER,$TITLE_COLUMN TEXT,$CATEGORY_ID_COLUMN INTEGER,$PRICE_COLUMN INTEGER,$ACCOUNT_ID_COLUMN INTEGER,$CURRENCY_COLUMN TEXT,$DECIMALS_COLUMN INTEGER);")
        db.execSQL("CREATE TABLE $TABLE_CATEGORIES($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT,$NAME_COLUMN TEXT);")
        db.execSQL("CREATE TABLE $TABLE_ACCOUNTS($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT,$CREATED_AT_COLUMN INTEGER,$TITLE_COLUMN TEXT,$CUR_SUM_COLUMN INTEGER,$CURRENCY_COLUMN TEXT,$DECIMALS_COLUMN INTEGER,$GOAL_COLUMN REAL,$ARCHIVED_COLUMN INTEGER,$COLOR_COLUMN INTEGER);")
        db.execSQL("CREATE TABLE $TABLE_TRANSFERS($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT,$TIME_COLUMN INTEGER,$FROM_ACCOUNT_ID_COLUMN INTEGER,$TO_ACCOUNT_ID_COLUMN INTEGER,$FROM_AMOUNT_COLUMN INTEGER,$TO_AMOUNT_COLUMN INTEGER,$DECIMALS_FROM_COLUMN INTEGER,$DECIMALS_TO_COLUMN INTEGER);")
        createRatesTable(db)
        insertDefaultAccount(db)
    }

    private fun createDbVersion6(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_RECORDS($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT,$TIME_COLUMN INTEGER,$TYPE_COLUMN INTEGER,$TITLE_COLUMN TEXT,$CATEGORY_ID_COLUMN INTEGER,$NOTES_COLUMN TEXT,$PRICE_COLUMN INTEGER,$ACCOUNT_ID_COLUMN INTEGER,$CURRENCY_COLUMN TEXT,$DECIMALS_COLUMN INTEGER);")
        db.execSQL("CREATE TABLE $TABLE_CATEGORIES($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT,$NAME_COLUMN TEXT);")
        db.execSQL("CREATE TABLE $TABLE_ACCOUNTS($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT,$CREATED_AT_COLUMN INTEGER,$TITLE_COLUMN TEXT,$CUR_SUM_COLUMN INTEGER,$CURRENCY_COLUMN TEXT,$DECIMALS_COLUMN INTEGER,$GOAL_COLUMN REAL,$ARCHIVED_COLUMN INTEGER,$COLOR_COLUMN INTEGER);")
        db.execSQL("CREATE TABLE $TABLE_TRANSFERS($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT,$TIME_COLUMN INTEGER,$FROM_ACCOUNT_ID_COLUMN INTEGER,$TO_ACCOUNT_ID_COLUMN INTEGER,$FROM_AMOUNT_COLUMN INTEGER,$TO_AMOUNT_COLUMN INTEGER,$DECIMALS_FROM_COLUMN INTEGER,$DECIMALS_TO_COLUMN INTEGER);")
        createRatesTable(db)
        insertDefaultAccount(db)
    }

    private fun createRatesTable(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_RATES($ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT,$CREATED_AT_COLUMN INTEGER,$FROM_CURRENCY_COLUMN INTEGER,$TO_CURRENCY_COLUMN INTEGER,$AMOUNT_COLUMN REAL);")
    }

    private fun insertDefaultAccount(db: SQLiteDatabase): Long {
        val contentValues = ContentValues()
        contentValues.put(TITLE_COLUMN, DEFAULT_ACCOUNT)
        contentValues.put(CUR_SUM_COLUMN, 0)
        contentValues.put(CURRENCY_COLUMN, DEFAULT_ACCOUNT_CURRENCY)
        contentValues.put(CREATED_AT_COLUMN, System.currentTimeMillis())
        return db.insert(TABLE_ACCOUNTS, null, contentValues)
    }

    companion object {
        /* DB_VERSION = 1 */
        const val DB_NAME = "database"
        const val DB_VERSION = 6
        const val TABLE_RECORDS = "records"
        const val TABLE_CATEGORIES = "categories"

        const val ID_COLUMN = "id"
        const val TIME_COLUMN = "time"
        const val TYPE_COLUMN = "type"
        const val TITLE_COLUMN = "title"
        const val CATEGORY_ID_COLUMN = "category_id"
        const val PRICE_COLUMN = "price"
        const val NAME_COLUMN = "name"

        /* DB_VERSION = 2 */
        const val TABLE_ACCOUNTS = "accounts"
        const val CURRENCY_COLUMN = "currency"
        const val ACCOUNT_ID_COLUMN = "account_id"
        const val CUR_SUM_COLUMN = "cur_sum"
        const val DEFAULT_ACCOUNT = "Default"
        const val DEFAULT_ACCOUNT_CURRENCY = "NON"
        const val TABLE_TRANSFERS = "transfers"
        const val FROM_ACCOUNT_ID_COLUMN = "from_account_id"
        const val TO_ACCOUNT_ID_COLUMN = "to_account_id"
        const val FROM_AMOUNT_COLUMN = "from_amount"
        const val TO_AMOUNT_COLUMN = "to_amount"
        const val CREATED_AT_COLUMN = "created_at"

        /* DB_VERSION = 3 */
        const val TABLE_RATES = "rates"
        const val FROM_CURRENCY_COLUMN = "from_currency"
        const val TO_CURRENCY_COLUMN = "to_currency"
        const val AMOUNT_COLUMN = "amount"

        /* DB_VERSION = 4 */
        const val DECIMALS_COLUMN = "decimals"
        const val DECIMALS_FROM_COLUMN = "decimals_from"
        const val DECIMALS_TO_COLUMN = "decimals_to"

        /* DB_VERSION = 5 */
        const val GOAL_COLUMN = "goal"
        const val ARCHIVED_COLUMN = "archived"
        const val COLOR_COLUMN = "color"

        /* DB_VERSION = 6 */
        const val NOTES_COLUMN = "notes"
    }
}
