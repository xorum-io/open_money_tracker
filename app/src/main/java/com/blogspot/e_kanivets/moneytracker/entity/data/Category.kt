package com.blogspot.e_kanivets.moneytracker.entity.data

import android.os.Parcel
import android.os.Parcelable
import com.blogspot.e_kanivets.moneytracker.entity.base.BaseEntity

/**
 * Entity class for account.
 * Created on 01/09/14.
 *
 * @author Evgenii Kanivets
 */
class Category : BaseEntity, Parcelable {
    val name: String?

    constructor(id: Long, name: String?) {
        this.id = id
        this.name = name
    }

    constructor(name: String?) {
        this.id = -1
        this.name = name
    }

    constructor(parcel: Parcel) {
        this.id = parcel.readLong()
        name = parcel.readString()
    }

    override fun equals(other: Any?): Boolean {
        if (other is Category) {
            return this.id == other.id && equals(this.name, other.name)
        }
        return false
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("Category {")
        sb.append("id = ").append(id).append(", ")
        sb.append("title = ").append(name)
        sb.append("}")
        return sb.toString()
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(name)
    }

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category = Category(parcel)
        override fun newArray(size: Int): Array<Category?> = arrayOfNulls(size)
    }
}
