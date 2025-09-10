package com.blogspot.e_kanivets.moneytracker.controller

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

class FormatController(private val preferenceController: PreferenceController) {

    companion object {
        const val PRECISION_MATH = "precision_math"
        const val PRECISION_INT = "precision_int"
        const val PRECISION_NONE = "precision_none"
        private val shortDateFormat = SimpleDateFormat("yyyy-MM-dd")
        private val fullDateFormat = SimpleDateFormat("d MMMM yyyy")
        private val timeFormat = SimpleDateFormat("HH:mm")
        private val dateAndTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
    }

    fun formatAmount(amount: Double): String = when (preferenceController.readDisplayPrecision()) {
        PRECISION_MATH -> formatPrecisionMath(amount)
        PRECISION_INT -> formatPrecisionInt(amount)
        PRECISION_NONE -> formatPrecisionNone(amount)
        else -> formatPrecisionMath(amount)
    }

    fun formatPrecisionMath(amount: Double): String = String.format(Locale.US, "%d", amount.roundToInt())

    fun formatPrecisionInt(amount: Double): String = String.format(Locale.US, "%d", amount.toInt())

    fun formatPrecisionNone(amount: Double): String = String.format(Locale.US, "%.2f", amount)

    fun formatSignedAmount(amount: Double): String = (if (amount >= 0.0) "+ " else "- ") + formatAmount(kotlin.math.abs(amount))

    fun formatIncome(amount: Double, currency: String): String = (if (amount >= 0) "+ " else "- ") + formatAmount(kotlin.math.abs(amount)) + " " + currency

    fun formatExpense(amount: Double, currency: String): String = (if (amount > 0) "+ " else "- ") + formatAmount(kotlin.math.abs(amount)) + " " + currency

    fun formatDateToNumber(timestamp: Long): String = shortDateFormat.format(Date(timestamp))

    fun formatDateToString(timestamp: Long): String = fullDateFormat.format(Date(timestamp))

    fun formatTime(timestamp: Long): String = timeFormat.format(Date(timestamp))

    fun formatDateAndTime(timestamp: Long): String = dateAndTimeFormat.format(Date(timestamp))
}
