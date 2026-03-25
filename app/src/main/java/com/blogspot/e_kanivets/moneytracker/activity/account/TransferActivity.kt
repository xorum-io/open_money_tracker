package com.blogspot.e_kanivets.moneytracker.activity.account

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity
import com.blogspot.e_kanivets.moneytracker.controller.data.AccountController
import com.blogspot.e_kanivets.moneytracker.controller.data.TransferController
import com.blogspot.e_kanivets.moneytracker.databinding.ActivityTransferBinding
import com.blogspot.e_kanivets.moneytracker.entity.data.Account
import com.blogspot.e_kanivets.moneytracker.entity.data.Transfer
import com.blogspot.e_kanivets.moneytracker.util.CrashlyticsProxy
import com.blogspot.e_kanivets.moneytracker.util.validator.IValidator
import com.blogspot.e_kanivets.moneytracker.util.validator.TransferValidator
import javax.inject.Inject

class TransferActivity : BaseBackActivity() {

    @Inject
    lateinit var transferController: TransferController

    @Inject
    lateinit var accountController: AccountController

    private lateinit var transferValidator: IValidator<Transfer>

    private lateinit var accountList: List<Account>

    private lateinit var binding: ActivityTransferBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initToolbar()
        initViews()
    }

    private fun initData(): Boolean {
        appComponent.inject(this)
        accountList = accountController.readActiveAccounts()
        return true
    }

    private fun initViews() {
        val accounts = mutableListOf<String>()
        for (account in accountList) {
            accounts.add(account.title)
        }

        transferValidator = TransferValidator(this, binding)

        if (accounts.isEmpty()) {
            accounts.add(getString(R.string.none))
            binding.spinnerFrom.isEnabled = false
            binding.spinnerTo.isEnabled = false
        }

        binding.spinnerFrom.adapter = ArrayAdapter(this, R.layout.view_spinner_item, accounts)
        binding.spinnerTo.adapter = ArrayAdapter(this, R.layout.view_spinner_item, accounts)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_transfer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_done -> {
            tryTransfer()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun tryTransfer() {
        CrashlyticsProxy.instance.logButton("Try Transfer")
        if (doTransfer()) {
            CrashlyticsProxy.instance.logEvent("Done Transfer")
            setResult(RESULT_OK)
            finish()
        }
    }

    @SuppressWarnings("SimplifiableIfStatement")
    private fun doTransfer() = if (transferValidator.validate()) {
        val fromAccount = accountList[binding.spinnerFrom.selectedItemPosition]
        val toAccount = accountList[binding.spinnerTo.selectedItemPosition]
        val fromAmount = binding.etFromAmount.text.toString().toDouble()
        val toAmount = binding.etToAmount.text.toString().toDouble()

        transferController.create(
            Transfer(
                System.currentTimeMillis(),
                fromAccount.id,
                toAccount.id,
                fromAmount,
                toAmount
            )
        ) != null
    } else {
        false
    }
}
