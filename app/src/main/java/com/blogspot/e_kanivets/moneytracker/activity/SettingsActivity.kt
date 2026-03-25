package com.blogspot.e_kanivets.moneytracker.activity

import android.os.Build
import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference.OnPreferenceChangeListener
import android.preference.PreferenceFragment
import com.blogspot.e_kanivets.moneytracker.BuildConfig
import com.blogspot.e_kanivets.moneytracker.MtApp
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity
import com.blogspot.e_kanivets.moneytracker.controller.CurrencyController
import com.blogspot.e_kanivets.moneytracker.controller.FormatController
import com.blogspot.e_kanivets.moneytracker.controller.PreferenceController
import com.blogspot.e_kanivets.moneytracker.controller.data.AccountController
import com.blogspot.e_kanivets.moneytracker.databinding.ActivitySettingsBinding
import com.blogspot.e_kanivets.moneytracker.entity.data.Account
import javax.inject.Inject

class SettingsActivity : BaseBackActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
        initViews()
    }

    private fun initViews() {
        // Display the fragment as the main content.
        fragmentManager.beginTransaction().replace(binding.contentView.id, SettingsFragment()).commit()
    }

    class SettingsFragment : PreferenceFragment() {

        @set:Inject
        var accountController: AccountController? = null

        @set:Inject
        var currencyController: CurrencyController? = null

        @set:Inject
        var preferenceController: PreferenceController? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            MtApp.instance.appComponent.inject(this@SettingsFragment)

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences)

            setupDefaultAccountPref()
            setupDefaultCurrencyPref()
            setupNonSubstitutionCurrencyPref()
            setupDisplayPrecision()
            setupAboutPref()
        }

        private fun setupDefaultAccountPref() {
            val defaultAccountPref =
                findPreference(getString(R.string.pref_default_account)) as ListPreference
            defaultAccountPref.onPreferenceChangeListener = preferenceChangeListener

            val accountList = accountController!!.readActiveAccounts()
            defaultAccountPref.entries = getEntries(accountList)
            defaultAccountPref.entryValues = getEntryValues(accountList)

            val defaultAccount = accountController!!.readDefaultAccount()
            if (defaultAccount == null) {
                defaultAccountPref.setDefaultValue("")
                defaultAccountPref.summary = ""
            } else {
                defaultAccountPref.setDefaultValue(defaultAccount.title)
                defaultAccountPref.summary = defaultAccount.title
            }
        }

        private fun setupDefaultCurrencyPref() {
            val defaultCurrencyPref =
                findPreference(getString(R.string.pref_default_currency)) as ListPreference
            defaultCurrencyPref.onPreferenceChangeListener = preferenceChangeListener

            val currencyList = currencyController!!.readAll()
            defaultCurrencyPref.entries = currencyList.toTypedArray<String>()
            defaultCurrencyPref.entryValues = currencyList.toTypedArray<String>()

            val defaultCurrency = currencyController!!.readDefaultCurrency()
            defaultCurrencyPref.setDefaultValue(defaultCurrency)
            defaultCurrencyPref.summary = defaultCurrency
        }

        private fun setupNonSubstitutionCurrencyPref() {
            val nonSubstitutionCurrencyPref =
                findPreference(getString(R.string.pref_non_substitution_currency)) as ListPreference
            nonSubstitutionCurrencyPref.onPreferenceChangeListener = preferenceChangeListener

            val currencyList = currencyController!!.readAll()
            nonSubstitutionCurrencyPref.entries = currencyList.toTypedArray<String>()
            nonSubstitutionCurrencyPref.entryValues = currencyList.toTypedArray<String>()

            val nonSubstitutionCurrency = preferenceController!!.readNonSubstitutionCurrency()
            nonSubstitutionCurrencyPref.setDefaultValue(nonSubstitutionCurrency)
            nonSubstitutionCurrencyPref.summary = nonSubstitutionCurrency
        }

        private fun setupDisplayPrecision() {
            val displayPrecisionPref =
                findPreference(getString(R.string.pref_display_precision)) as ListPreference
            displayPrecisionPref.onPreferenceChangeListener = preferenceChangeListener

            val precisionListValues: MutableList<String> = ArrayList()
            precisionListValues.add(FormatController.PRECISION_MATH)
            precisionListValues.add(FormatController.PRECISION_INT)
            precisionListValues.add(FormatController.PRECISION_NONE)
            displayPrecisionPref.entryValues = precisionListValues.toTypedArray<String>()

            val precisionList: MutableList<String> = ArrayList()
            precisionList.add(getString(R.string.precision_math))
            precisionList.add(getString(R.string.precision_int))
            precisionList.add(getString(R.string.precision_none))
            displayPrecisionPref.entries = precisionList.toTypedArray<String>()

            if (FormatController.PRECISION_MATH == preferenceController!!.readDisplayPrecision()) {
                displayPrecisionPref.setDefaultValue(getString(R.string.precision_math))
                displayPrecisionPref.summary = getString(R.string.precision_math)
            }
        }

        private fun setupAboutPref() {
            val preference = findPreference(getString(R.string.pref_about))
            preference.summary = getString(
                R.string.about_summary,
                BuildConfig.VERSION_NAME,
                Build.VERSION.RELEASE
            )
        }

        private fun getEntries(accountList: List<Account>): Array<String> {
            val result: MutableList<String> = ArrayList()

            for (account in accountList) {
                result.add(account.title)
            }

            return result.toTypedArray<String>()
        }

        private fun getEntryValues(accountList: List<Account>): Array<String> {
            val result: MutableList<String> = ArrayList()

            for (account in accountList) {
                result.add(account.id.toString())
            }

            return result.toTypedArray<String>()
        }

        private val preferenceChangeListener =
            OnPreferenceChangeListener { preference, newValue -> // Previously we could set summary to default value,
                // but now it's needed to display selected entry
                preference.summary = "%s"
                activity.setResult(RESULT_OK)
                true
            }
    }

    companion object {
        @Suppress("unused")
        private const val TAG = "SettingsActivity"
    }
}
