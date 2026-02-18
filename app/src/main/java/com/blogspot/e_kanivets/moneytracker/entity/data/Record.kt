package com.blogspot.e_kanivets.moneytracker.entity.data

import android.os.Parcel
import android.os.Parcelable
import com.blogspot.e_kanivets.moneytracker.entity.base.BaseEntity
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper

/**
 * Entity class. Immutable.
 * Created by eugene on 01/09/14.
 */
class Record : BaseEntity, Parcelable {
    val time: Long
    val type: Int
    val title: String?
    val category: Category?
    val notes: String?
    val price: Long
    val account: Account?
    val currency: String
    val decimals: Long

    constructor(
        id: Long, time: Long, type: Int, title: String?, categoryId: Long, notes: String?,
        price: Long, accountId: Long, currency: String?, decimals: Long
    ) {
        this.id = id
        this.time = time
        this.type = type
        this.title = title
        this.category = Category(categoryId, null)
        this.notes = notes
        this.price = price
        this.account = Account(accountId, "", -1, "", 0, -1.0, false, -1)
        this.currency = currency ?: DbHelper.DEFAULT_ACCOUNT_CURRENCY
        this.decimals = decimals
    }

    constructor(
        id: Long, time: Long, type: Int, title: String?, category: Category?, notes: String?,
        price: Long, account: Account?, currency: String?, decimals: Long
    ) {
        this.id = id
        this.time = time
        this.type = type
        this.title = title
        this.category = category
        this.notes = notes
        this.price = price
        this.account = account
        this.currency = currency ?: DbHelper.DEFAULT_ACCOUNT_CURRENCY
        this.decimals = decimals
    }

    constructor(
        id: Long, time: Long, type: Int, title: String?, category: Category?, notes: String?,
        price: Double, account: Account?, currency: String?
    ) {
        this.id = id
        this.time = time
        this.type = type
        this.title = title
        this.category = category
        this.notes = notes
        this.account = account
        this.currency = currency ?: DbHelper.DEFAULT_ACCOUNT_CURRENCY
        this.price = getLong(price)
        this.decimals = getDecimal(price)
    }

    constructor(
        time: Long, type: Int, title: String?, category: Category?, notes: String?,
        price: Double, account: Account?, currency: String?
    ) {
        this.id = -1L
        this.time = time
        this.type = type
        this.title = title
        this.category = category
        this.notes = notes
        this.account = account
        this.currency = currency ?: DbHelper.DEFAULT_ACCOUNT_CURRENCY
        this.price = getLong(price)
        this.decimals = getDecimal(price)
    }

    constructor(parcel: Parcel) {
        this.id = parcel.readLong()
        time = parcel.readLong()
        type = parcel.readInt()
        title = parcel.readString()
        category = parcel.readParcelable(Category::class.java.classLoader)
        notes = parcel.readString()
        price = parcel.readLong()
        account = parcel.readParcelable(Account::class.java.classLoader)
        currency = parcel.readString() ?: DbHelper.DEFAULT_ACCOUNT_CURRENCY
        decimals = parcel.readLong()
    }

    val fullPrice: Double get() = price + decimals / 100.0

    val isIncome: Boolean get() = type == 0

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("Record {")
        sb.append("id = ").append(id).append(", ")
        sb.append("title = ").append(title).append(", ")
        sb.append("type = ")
        when (type) {
            TYPE_EXPENSE -> sb.append("expense")
            TYPE_INCOME -> sb.append("income")
            else -> sb.append("unknown")
        }
        sb.append(", ")
        sb.append("time = ").append(time).append(", ")
        sb.append("category = ").append(category).append(", ")
        sb.append("notes = ").append(notes).append(", ")
        sb.append("price = ").append(price).append(", ")
        sb.append("account = ").append(account).append(", ")
        sb.append("currency = ").append(currency).append(", ")
        sb.append("decimals = ").append(decimals)
        sb.append("}")
        return sb.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (other is Record) {
            return this.id == other.id
                    && this.time == other.time
                    && this.type == other.type
                    && equals(this.title, other.title)
                    && this.category == other.category
                    && equals(this.notes, other.notes)
                    && this.price == other.price
                    && this.account == other.account
                    && equals(this.currency, other.currency)
                    && this.decimals == other.decimals
        }
        return false
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeLong(time)
        dest.writeInt(type)
        dest.writeString(title)
        dest.writeParcelable(category, 0)
        dest.writeString(notes)
        dest.writeLong(price)
        dest.writeParcelable(account, 0)
        dest.writeString(currency)
        dest.writeLong(decimals)
    }

    companion object CREATOR : Parcelable.Creator<Record> {
        const val TYPE_INCOME = 0
        const val TYPE_EXPENSE = 1

        override fun createFromParcel(parcel: Parcel): Record = Record(parcel)
        override fun newArray(size: Int): Array<Record?> = arrayOfNulls(size)
    }
}
