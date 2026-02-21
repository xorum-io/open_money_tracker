package com.blogspot.e_kanivets.moneytracker.controller

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper
import java.util.HashSet
import java.util.TreeMap
import java.util.TreeSet

/**
 * Controller class to encapsulate Shared Preferences handling logic.
 * Not deal with [com.blogspot.e_kanivets.moneytracker.repo.base.IRepo] instances as others.
 * Created on 4/21/16.
 *
 * @author Evgenii Kanivets
 */
class PreferenceController(private val context: Context) {

    fun addLaunchCount() {
        val preferences = getDefaultPrefs()
        val editor = getEditor()
        editor.putInt(LAUNCH_COUNT, preferences.getInt(LAUNCH_COUNT, 0) + 1)
        editor.apply()
    }

    fun checkRateDialog(): Boolean {
        val preferences = getDefaultPrefs()
        val appRated = preferences.getBoolean(APP_RATED, false)
        if (appRated) return false
        val launchCount = preferences.getInt(LAUNCH_COUNT, 0)
        return launchCount % RATE_PERIOD == 0
    }

    fun appRated() {
        val editor = getEditor()
        editor.putBoolean(APP_RATED, true)
        editor.apply()
    }

    fun writeFirstTs(firstTs: Long) {
        val editor = getEditor()
        editor.putLong(KEY_FIRST_TS, firstTs)
        editor.apply()
    }

    fun writeLastTs(lastTs: Long) {
        val editor = getDefaultPrefs().edit()
        editor.putLong(KEY_LAST_TS, lastTs)
        editor.apply()
    }

    fun writePeriodType(periodType: String) {
        val editor = getEditor()
        editor.putString(KEY_PERIOD_TYPE, periodType)
        editor.apply()
    }

    fun writeDropboxAccessToken(accessToken: String?) {
        val editor = getEditor()
        editor.putString(KEY_DROPBOX_ACCESS_TOKEN, accessToken)
        editor.apply()
    }

    fun writeFilteredCategories(categorySet: Set<String>?) {
        val editor = getEditor()
        editor.putStringSet(KEY_FILTERED_CATEGORIES, categorySet)
        editor.apply()
    }

    fun writeRecordTitleCategoryPairs(map: Map<String, String>) {
        val set = TreeSet<String>()
        for (key in map.keys) {
            set.add("$key;${map[key]}")
        }
        val editor = getEditor()
        editor.putStringSet(KEY_RECORD_TITLE_CATEGORY_PAIRS, set)
        editor.apply()
    }

    fun readDefaultAccountId(): Long {
        val defaultAccountPref = context.getString(R.string.pref_default_account)
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(defaultAccountPref, "-1")!!.toLong()
    }

    fun readDefaultCurrency(): String? {
        val defaultCurrencyPref = context.getString(R.string.pref_default_currency)
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(defaultCurrencyPref, null)
    }

    fun readDisplayPrecision(): String {
        val displayPrecisionPref = context.getString(R.string.pref_display_precision)
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(displayPrecisionPref, FormatController.PRECISION_MATH)!!
    }

    fun readNonSubstitutionCurrency(): String? {
        val nonSubstitutionCurrencyPref = context.getString(R.string.pref_non_substitution_currency)
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(nonSubstitutionCurrencyPref, DbHelper.DEFAULT_ACCOUNT_CURRENCY)
    }

    fun readFirstTs(): Long = getDefaultPrefs().getLong(KEY_FIRST_TS, -1)

    fun readLastTs(): Long = getDefaultPrefs().getLong(KEY_LAST_TS, -1)

    fun readPeriodType(): String? = getDefaultPrefs().getString(KEY_PERIOD_TYPE, null)

    fun readDropboxAccessToken(): String? = getDefaultPrefs().getString(KEY_DROPBOX_ACCESS_TOKEN, null)

    fun readFilteredCategories(): Set<String> {
        // http://stackoverflow.com/questions/14034803/misbehavior-when-trying-to-store-a-string-set-using-sharedpreferences/14034804#14034804
        return HashSet(getDefaultPrefs().getStringSet(KEY_FILTERED_CATEGORIES, HashSet()))
    }

    fun readRecordTitleCategoryPairs(): Map<String, String> {
        val map = TreeMap<String, String>()
        val set = getDefaultPrefs().getStringSet(KEY_RECORD_TITLE_CATEGORY_PAIRS, HashSet()) ?: return map
        for (entry in set) {
            val words = entry.split(";")
            if (words.size == 2) {
                map[words[0]] = words[1]
            }
        }
        return map
    }

    private fun getDefaultPrefs(): SharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    private fun getEditor(): SharedPreferences.Editor = getDefaultPrefs().edit()

    companion object {
        private const val APP_RATED = "app_rated"
        private const val LAUNCH_COUNT = "launch_count"
        private const val KEY_FIRST_TS = "key_first_ts"
        private const val KEY_LAST_TS = "key_last_ts"
        private const val KEY_PERIOD_TYPE = "key_period_type"
        private const val KEY_DROPBOX_ACCESS_TOKEN = "key_dropbox_access_token"
        private const val KEY_FILTERED_CATEGORIES = "key_filtered_categories"
        private const val KEY_RECORD_TITLE_CATEGORY_PAIRS = "key_record_title_category_pairs"

        private const val RATE_PERIOD = 5
    }
}
