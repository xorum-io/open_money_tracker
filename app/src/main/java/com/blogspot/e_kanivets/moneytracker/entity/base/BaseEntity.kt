package com.blogspot.e_kanivets.moneytracker.entity.base

abstract class BaseEntity : IEntity {
    override var id: Long = 0
        protected set

    protected fun equals(str1: String?, str2: String?): Boolean =
        if (str1 == null) str2 == null else str1 == str2

    protected fun getLong(value: Double): Long = value.toLong()

    protected fun getDecimal(value: Double): Long =
        // Strange calculation because of double type precision issue
        Math.round(value * 100 - getLong(value) * 100)
}
