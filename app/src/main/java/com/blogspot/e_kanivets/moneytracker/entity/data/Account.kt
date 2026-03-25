package com.blogspot.e_kanivets.moneytracker.entity.data

import android.os.Parcel
import android.os.Parcelable
import com.blogspot.e_kanivets.moneytracker.entity.base.BaseEntity

/**
 * Entity class for account.
 * Created on 6/3/15.
 * @author Evgenii Kanivets
 */
class Account : BaseEntity, Parcelable {
    val title: String
    var curSum: Long
    val currency: String
    var decimals: Long
    var goal: Double
    var archived: Boolean
    var color: Int

    constructor(
        id: Long,
        title: String,
        curSum: Long,
        currency: String,
        decimals: Long,
        goal: Double,
        archived: Boolean,
        color: Int
    ) {
        this.id = id
        this.title = title
        this.curSum = curSum
        this.currency = currency
        this.decimals = decimals
        this.goal = goal
        this.archived = archived
        this.color = color
    }

    constructor(
        id: Long,
        title: String,
        curSum: Double,
        currency: String,
        goal: Double,
        archived: Boolean,
        color: Int
    ) {
        this.id = id
        this.title = title
        this.currency = currency
        this.curSum = getLong(curSum)
        this.decimals = getDecimal(curSum)
        this.goal = goal
        this.archived = archived
        this.color = color
    }

    constructor(parcel: Parcel) {
        id = parcel.readLong()
        title = parcel.readString() ?: ""
        curSum = parcel.readLong()
        currency = parcel.readString() ?: ""
        decimals = parcel.readLong()
        goal = parcel.readDouble()
        archived = parcel.readByte() != 0.toByte()
        color = parcel.readInt()
    }

    val fullSum: Double
        get() = curSum + decimals / 100.0

    fun put(amount: Double) {
        var sum = fullSum
        sum += amount
        curSum = getLong(sum)
        decimals = getDecimal(sum)
    }

    fun take(amount: Double) {
        var sum = fullSum
        sum -= amount
        curSum = getLong(sum)
        decimals = getDecimal(sum)
    }

    fun archive() {
        archived = true
    }

    fun restore() {
        archived = false
    }

    override fun equals(other: Any?): Boolean {
        if (other is Account) {
            return this.id == other.id &&
                    this.title == other.title &&
                    this.curSum == other.curSum &&
                    this.currency == other.currency &&
                    this.decimals == other.decimals &&
                    this.goal == other.goal &&
                    this.archived == other.archived &&
                    this.color == other.color
        }
        return false
    }

    override fun toString(): String {
        return "Account {id = $id, title = $title, curSum = $curSum, currency = $currency, decimals = $decimals, goal = $goal, archived = $archived, color = $color}"
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(title)
        dest.writeLong(curSum)
        dest.writeString(currency)
        dest.writeLong(decimals)
        dest.writeDouble(goal)
        dest.writeByte(if (archived) 1 else 0)
        dest.writeInt(color)
    }

    val isArchived: Boolean
        get() = archived

    companion object CREATOR : Parcelable.Creator<Account> {
        override fun createFromParcel(parcel: Parcel): Account = Account(parcel)
        override fun newArray(size: Int): Array<Account?> = arrayOfNulls(size)
    }
} 