package com.blogspot.e_kanivets.moneytracker.activity.account

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.annotation.Nullable
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity
import com.blogspot.e_kanivets.moneytracker.controller.data.AccountController
import com.blogspot.e_kanivets.moneytracker.controller.CurrencyController
import com.blogspot.e_kanivets.moneytracker.databinding.ActivityAddAccountBinding
import com.blogspot.e_kanivets.moneytracker.entity.data.Account
import com.blogspot.e_kanivets.moneytracker.util.CrashlyticsProxy
import com.blogspot.e_kanivets.moneytracker.util.validator.AccountValidator
import com.blogspot.e_kanivets.moneytracker.util.validator.IValidator
import java.util.ArrayList
import javax.inject.Inject

class AddAccountActivity : BaseBackActivity() {

    @Inject
    lateinit var accountController: AccountController

    @Inject
    lateinit var currencyController: CurrencyController

    private lateinit var accountValidator: IValidator<Account>

    private lateinit var binding: ActivityAddAccountBinding

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initToolbar()
        initViews()
    }

    private fun initData(): Boolean {
        appComponent.inject(this)
        return true
    }

    private fun initViews() {
        accountValidator = AccountValidator(this, binding)
        binding.spinner.adapter = ArrayAdapter(
            this,
            R.layout.view_spinner_item,
            ArrayList(currencyController.readAll())
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_account, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_done -> {
            tryAddAccount()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun tryAddAccount() {
        CrashlyticsProxy.instance.logButton("Try Account")
        if (addAccount()) {
            CrashlyticsProxy.instance.logEvent("Done Account")
            setResult(RESULT_OK)
            finish()
        }
    }

    @SuppressWarnings("SimplifiableIfStatement")
    private fun addAccount() = if (accountValidator.validate()) {
        val title = binding.etTitle.text.toString().trim()
        val initSum = binding.etInitSum.text.toString().trim().toDouble()
        val currency = binding.spinner.selectedItem as String
        val goal = 0.0
        val color = 0

        val account = Account(-1, title, initSum, currency, goal, false, color)
        accountController.create(account) != null
    } else {
        false
    }
}
