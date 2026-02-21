package com.blogspot.e_kanivets.moneytracker.util.validator

interface IValidator<T> {
    companion object {
        const val MAX_ABS_VALUE = Integer.MAX_VALUE * 1024L
    }

    fun validate(): Boolean
}
