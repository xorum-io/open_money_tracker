package com.blogspot.e_kanivets.moneytracker.controller.data

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.blogspot.e_kanivets.moneytracker.controller.PreferenceController
import com.blogspot.e_kanivets.moneytracker.controller.base.BaseController
import com.blogspot.e_kanivets.moneytracker.entity.data.Account
import com.blogspot.e_kanivets.moneytracker.entity.data.Record
import com.blogspot.e_kanivets.moneytracker.entity.data.Transfer
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo

class AccountController(
    accountRepo: IRepo<Account>,
    private val preferenceController: PreferenceController
) : BaseController<Account>(accountRepo) {

    override fun read(id: Long): Account? = substituteCurrency(super.read(id))

    override fun readAll(): List<Account> = super.readAll().mapNotNull { substituteCurrency(it) }

    fun readActiveAccounts(): List<Account> = readAll().filter { !it.isArchived }

    fun readArchivedAccounts(): List<Account> = readAll().filter { it.isArchived }

    fun recordAdded(record: Record?): Boolean {
        val accountId = record?.account?.id ?: return false
        val account = repo.read(accountId) ?: return false
        when (record.type) {
            Record.TYPE_EXPENSE -> account.take(record.fullPrice)
            Record.TYPE_INCOME -> account.put(record.fullPrice)
        }
        repo.update(account)
        return true
    }

    fun recordDeleted(record: Record?): Boolean {
        if (record == null) return false
        val accountId = record.account?.id ?: return true
        val account = repo.read(accountId) ?: return false
        when (record.type) {
            Record.TYPE_EXPENSE -> account.put(record.fullPrice)
            Record.TYPE_INCOME -> account.take(record.fullPrice)
        }
        repo.update(account)
        return true
    }

    fun recordUpdated(oldRecord: Record?, newRecord: Record?): Boolean {
        if (oldRecord == null || newRecord == null) return false
        return recordDeleted(oldRecord) && recordAdded(newRecord)
    }

    fun transferDone(transfer: Transfer?): Boolean {
        if (transfer == null) return false
        val fromAccount = repo.read(transfer.fromAccountId) ?: return false
        val toAccount = repo.read(transfer.toAccountId) ?: return false
        fromAccount.take(transfer.fullFromAmount)
        toAccount.put(transfer.fullToAmount)
        repo.update(fromAccount)
        repo.update(toAccount)
        return true
    }

    fun readDefaultAccount(): Account? {
        val defaultAccountId = preferenceController.readDefaultAccountId()
        return if (defaultAccountId == -1L) {
            getFirstAccount()
        } else {
            val account = read(defaultAccountId)
            account ?: getFirstAccount()
        }
    }

    fun archive(account: Account?): Boolean {
        return if (account == null) {
            false
        } else {
            account.archive()
            update(account)
            true
        }
    }

    fun restore(account: Account?): Boolean {
        return if (account == null) {
            false
        } else {
            account.restore()
            update(account)
            true
        }
    }

    private fun getFirstAccount(): Account? {
        val accountList = readAll()
        return if (accountList.isEmpty()) null else accountList[0]
    }

    private fun substituteCurrency(account: Account?): Account? {
        if (account == null) return null
        var currency = account.currency
        if (DbHelper.DEFAULT_ACCOUNT_CURRENCY == currency) {
            currency = preferenceController.readNonSubstitutionCurrency() ?: ""
        }
        return Account(
            account.id,
            account.title,
            account.curSum,
            currency,
            account.decimals,
            account.goal,
            account.isArchived,
            account.color
        )
    }
} 