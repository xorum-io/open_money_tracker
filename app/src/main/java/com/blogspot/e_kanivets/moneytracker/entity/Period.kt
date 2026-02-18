package com.blogspot.e_kanivets.moneytracker.entity

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Entity class for Period which consists from two dates. Immutable.
 * Created on 10/09/14.
 *
 * @author Evgenii Kanivets
 */
class Period(first: Date, last: Date, val type: String) : Parcelable {
    val first: Date = Date(first.time)
    val last: Date = Date(last.time)

    constructor(parcel: Parcel) : this(
        Date(parcel.readLong()),
        Date(parcel.readLong()),
        parcel.readString()!!
    )

    val firstDay: String get() = dateFormat.format(first)

    val lastDay: String get() = dateFormat.format(last)

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(first.time)
        dest.writeLong(last.time)
        dest.writeString(type)
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Period) {
            first == other.first && last == other.last
        } else false
    }

    companion object CREATOR : Parcelable.Creator<Period> {
        const val TYPE_DAY = "day"
        const val TYPE_WEEK = "week"
        const val TYPE_MONTH = "month"
        const val TYPE_YEAR = "year"
        const val TYPE_ALL_TIME = "all_time"
        const val TYPE_CUSTOM = "custom"

        @SuppressLint("SimpleDateFormat")
        private val dateFormat = SimpleDateFormat("dd MMM, yyyy")

        override fun createFromParcel(parcel: Parcel): Period = Period(parcel)
        override fun newArray(size: Int): Array<Period?> = arrayOfNulls(size)
    }
}
