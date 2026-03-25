package com.blogspot.e_kanivets.moneytracker.controller.data

import com.blogspot.e_kanivets.moneytracker.controller.PreferenceController
import com.blogspot.e_kanivets.moneytracker.controller.base.BaseController
import com.blogspot.e_kanivets.moneytracker.entity.Period
import com.blogspot.e_kanivets.moneytracker.entity.data.Account
import com.blogspot.e_kanivets.moneytracker.entity.data.Record
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo

/**
 * Controller class to encapsulate record handling logic.
 * Created on 1/22/16.
 *
 * @author Evgenii Kanivets
 */
class RecordController(
    recordRepo: IRepo<Record>,
    private val categoryController: CategoryController,
    private val accountController: AccountController,
    private val preferenceController: PreferenceController
) : BaseController<Record>(recordRepo) {

    override fun create(record: Record?): Record? {
        if (record == null) return null

        val validated = validateRecord(record)

        val createdRecord = repo.create(validated)
        return if (createdRecord == null) {
            null
        } else {
            accountController.recordAdded(createdRecord)
            createdRecord
        }
    }

    override fun update(record: Record?): Record? {
        if (record == null) return null

        val validated = validateRecord(record)

        val oldRecord = read(validated.id)

        val updatedRecord = repo.update(validated)
        return if (updatedRecord == null) {
            null
        } else {
            accountController.recordUpdated(oldRecord, updatedRecord)
            updatedRecord
        }
    }

    override fun delete(record: Record?): Boolean {
        return if (repo.delete(record)) {
            accountController.recordDeleted(record)
        } else {
            false
        }
    }

    override fun read(id: Long): Record? {
        val list = readWithCondition("id=?", arrayOf<String?>(id.toString()))

        return if (list.size == 1) list[0] else null
    }

    override fun readAll(): List<Record> {
        return readWithCondition(null, null)
    }

    override fun readWithCondition(condition: String?, args: Array<String?>?): List<Record> {
        val recordList = super.readWithCondition(condition, args)

        // Sort record list by time field from smallest to biggest
        val sorted = recordList.sortedWith(Comparator { lhs, rhs ->
            if (lhs.time < rhs.time) -1 else if (lhs.time == rhs.time) 0 else 1
        })

        // Data read from DB through Repo layer doesn't contain right nested objects, so construct them
        val completedRecordList = mutableListOf<Record>()
        for (record in sorted) {
            val category = if (record.category != null) categoryController.read(record.category.id) else null

            val account = if (record.account != null) accountController.read(record.account.id) else null

            var currency = record.currency
            if (DbHelper.DEFAULT_ACCOUNT_CURRENCY == currency) {
                currency = preferenceController.readNonSubstitutionCurrency() ?: currency
            }

            completedRecordList.add(
                Record(
                    record.id, record.time, record.type, record.title, category,
                    record.notes, record.price, account, currency, record.decimals
                )
            )
        }

        return completedRecordList
    }

    fun getRecordsForPeriod(period: Period): List<Record> {
        val condition = "${DbHelper.TIME_COLUMN} BETWEEN ? AND ?"
        val args = arrayOf<String?>(period.first.time.toString(), period.last.time.toString())

        return readWithCondition(condition, args)
    }

    fun getRecordsForAccount(account: Account): List<Record> {
        val condition = "${DbHelper.ACCOUNT_ID_COLUMN}=?"
        val args = arrayOf<String?>(account.id.toString())

        return readWithCondition(condition, args)
    }

    private fun validateRecord(record: Record): Record {
        if (record.category == null) return record

        val category = categoryController.readOrCreate(record.category.name)

        return Record(
            record.id, record.time, record.type, record.title, category,
            record.notes, record.price, record.account, record.currency, record.decimals
        )
    }
}
