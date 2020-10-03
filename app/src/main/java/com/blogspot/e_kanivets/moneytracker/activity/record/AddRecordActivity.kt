package com.blogspot.e_kanivets.moneytracker.activity.record

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import android.text.InputFilter
import android.text.Spanned
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity
import com.blogspot.e_kanivets.moneytracker.adapter.CategoryAutoCompleteAdapter
import com.blogspot.e_kanivets.moneytracker.controller.FormatController
import com.blogspot.e_kanivets.moneytracker.controller.PreferenceController
import com.blogspot.e_kanivets.moneytracker.controller.data.AccountController
import com.blogspot.e_kanivets.moneytracker.controller.data.CategoryController
import com.blogspot.e_kanivets.moneytracker.controller.data.RecordController
import com.blogspot.e_kanivets.moneytracker.entity.data.Account
import com.blogspot.e_kanivets.moneytracker.entity.data.Category
import com.blogspot.e_kanivets.moneytracker.entity.data.Record
import com.blogspot.e_kanivets.moneytracker.ui.AddRecordUiDecorator
import com.blogspot.e_kanivets.moneytracker.util.CategoryAutoCompleter
import com.blogspot.e_kanivets.moneytracker.util.CrashlyticsProxy
import com.blogspot.e_kanivets.moneytracker.util.validator.IValidator
import com.blogspot.e_kanivets.moneytracker.util.validator.RecordValidator
import kotlinx.android.synthetic.main.activity_add_record.*
import kotlinx.android.synthetic.main.content_add_record.*
import java.util.*
import javax.inject.Inject

class AddRecordActivity : BaseBackActivity() {

    private var record: Record? = null
    private var mode: Mode? = null
    private var type: Int = 0

    private var accountList: List<Account> = listOf()
    private var timestamp: Long = 0

    @Inject
    lateinit var categoryController: CategoryController
    @Inject
    lateinit var recordController: RecordController
    @Inject
    lateinit var accountController: AccountController
    @Inject
    lateinit var formatController: FormatController
    @Inject
    lateinit var preferenceController: PreferenceController

    private lateinit var recordValidator: IValidator<Record>
    private lateinit var uiDecorator: AddRecordUiDecorator
    private lateinit var autoCompleter: CategoryAutoCompleter

    override fun getContentViewId() = R.layout.activity_add_record


    override fun initData(): Boolean {
        super.initData()
        appComponent.inject(this)

        record = intent.getParcelableExtra(KEY_RECORD)
        mode = intent.getSerializableExtra(KEY_MODE) as Mode
        type = intent.getIntExtra(KEY_TYPE, -1)
        accountList = accountController.readActiveAccounts()

        timestamp = record?.time ?: Date().time

        return (mode != null && (type == Record.TYPE_INCOME || type == Record.TYPE_EXPENSE)
                && ((mode == Mode.MODE_EDIT && record != null) || (mode == Mode.MODE_ADD && record == null)))
    }

    @SuppressLint("SetTextI18n")
    override fun initViews() {
        super.initViews()

        recordValidator = RecordValidator(this, contentView)
        autoCompleter = CategoryAutoCompleter(categoryController, preferenceController)
        uiDecorator = AddRecordUiDecorator(this)

        uiDecorator.decorateActionBar(supportActionBar, mode, type)

        if (mode == Mode.MODE_EDIT) {
            record?.let { record ->
                etTitle.setText(record.title)
                etCategory.setText(record.category?.name.orEmpty())
                etPrice.setText(formatController.formatPrecisionNone(record.fullPrice))
            }
        }

        initCategoryAutocomplete()
        presentSpinnerAccount()

        // Restrict ';' for input, because it's used as delimiter when exporting
        etTitle.filters = arrayOf<InputFilter>(SemicolonInputFilter())
        etCategory.filters = arrayOf<InputFilter>(SemicolonInputFilter())

        tvDate.setOnClickListener { selectDate() }
        tvTime.setOnClickListener { selectTime() }

        if (type == Record.TYPE_EXPENSE) {
            fabDone.backgroundTintList = (getColorForFab(R.color.red_light))
        } else {
            fabDone.backgroundTintList = (getColorForFab(R.color.green_light))
        }

        fabDone.setOnClickListener { tryRecord() }

        updateDateAndTime()
    }

    private fun getColorForFab(color: Int): ColorStateList {
        return ColorStateList.valueOf(ContextCompat.getColor(this, color))
    }

    private fun initCategoryAutocomplete() {
        val categoryAutoCompleteAdapter = CategoryAutoCompleteAdapter(
                this, R.layout.view_category_item, autoCompleter)
        etCategory.setAdapter(categoryAutoCompleteAdapter)
        etCategory.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            etCategory.setText(parent.adapter.getItem(position) as String)
            etCategory.setSelection(etCategory.text.length)
        }
        etCategory.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) tryRecord()
            false
        }
        etCategory.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus && etCategory.text.toString().trim().isEmpty()) {
                val title = etTitle.text.toString().trim()
                autoCompleter.completeByRecordTitle(title)?.let { prediction ->
                    etCategory.setText(prediction)
                    etCategory.selectAll()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_record, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        when (mode) {
            Mode.MODE_ADD -> menu.removeItem(R.id.action_delete)
            else -> {
            }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                if (recordController.delete(record)) {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun selectDate() {
        CrashlyticsProxy.get().logButton("Select Date")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        val dialog = DatePickerDialog(this, uiDecorator.getTheme(type),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val newCalendar = Calendar.getInstance()
                    newCalendar.timeInMillis = timestamp
                    newCalendar.set(Calendar.YEAR, year)
                    newCalendar.set(Calendar.MONTH, monthOfYear)
                    newCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    if (newCalendar.timeInMillis < Date().time) {
                        timestamp = newCalendar.timeInMillis
                        updateDateAndTime()
                    } else {
                        showToast(R.string.record_in_future)
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
        dialog.show()
    }

    private fun selectTime() {
        CrashlyticsProxy.get().logButton("Show Time")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        val dialog = TimePickerDialog(this, uiDecorator.getTheme(type),
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    val newCalendar = Calendar.getInstance()
                    newCalendar.timeInMillis = timestamp
                    newCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    newCalendar.set(Calendar.MINUTE, minute)

                    if (newCalendar.timeInMillis < Date().time) {
                        timestamp = newCalendar.timeInMillis
                        updateDateAndTime()
                    } else {
                        showToast(R.string.record_in_future)
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(this))
        dialog.show()
    }

    private fun presentSpinnerAccount() {
        val accounts = accountList.map { it.title }.toMutableList()

        var selectedAccountIndex = -1

        if (mode == Mode.MODE_EDIT) {
            if (record?.account != null) {
                selectedAccountIndex = accountList.indexOf(accountList.find { it.id == record?.account?.id })
            }
        } else if (mode == Mode.MODE_ADD) {
            val defaultAccount = accountController.readDefaultAccount()
            selectedAccountIndex = accountList.indexOf(accountList.find { it.id == defaultAccount?.id })
        }

        if (selectedAccountIndex == -1) {
            spinnerAccount.isEnabled = false

            accounts.clear()
            accounts.add(getString(R.string.account_removed))
        }

        spinnerAccount.adapter = ArrayAdapter(this, R.layout.view_spinner_item, accounts)
        spinnerAccount.setSelection(selectedAccountIndex)
    }

    private fun tryRecord() {
        CrashlyticsProxy.get().logButton("Done Record")
        if (addRecord()) {
            CrashlyticsProxy.get().logEvent("Done Record")
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    private fun addRecord(): Boolean {
        if (recordValidator.validate()) {
            val now = Date().time
            if (timestamp > now) {
                showToast(R.string.record_in_future)
                return false
            }

            var title = etTitle.text.toString().trim()
            val category = etCategory.text.toString().trim()
            val price = etPrice.text.toString().toDouble()
            val account = accountList[spinnerAccount.selectedItemPosition]

            if (title.isEmpty()) {
                title = category
            }

            if (mode == Mode.MODE_ADD) {
                recordController.create(Record(timestamp, type, title,
                        Category(category), price, account, account.currency))
            } else if (mode == Mode.MODE_EDIT) {
                recordController.update(Record(record?.id ?: -1,
                        timestamp, type, title, Category(category), price, account, account.currency))
            }

            autoCompleter.addRecordTitleCategoryPair(title, category)
            return true
        } else {
            return false
        }
    }

    private fun updateDateAndTime() {
        tvDate.text = formatController.formatDateToNumber(timestamp)
        tvTime.text = formatController.formatTime(timestamp)
    }

    enum class Mode { MODE_ADD, MODE_EDIT }

    private class SemicolonInputFilter : InputFilter {

        override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {
            return if (source != null && ";" == source.toString()) "" else null
        }
    }

    companion object {
        const val KEY_RECORD = "key_record"
        const val KEY_MODE = "key_mode"
        const val KEY_TYPE = "key_type"
    }
}
