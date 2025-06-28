package com.blogspot.e_kanivets.moneytracker.activity.account

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.activity.account.edit.EditAccountActivity
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity
import com.blogspot.e_kanivets.moneytracker.adapter.AccountAdapter
import com.blogspot.e_kanivets.moneytracker.controller.data.AccountController
import com.blogspot.e_kanivets.moneytracker.databinding.ActivityAccountsBinding
import com.blogspot.e_kanivets.moneytracker.ui.presenter.AccountsSummaryPresenter
import com.blogspot.e_kanivets.moneytracker.util.CrashlyticsProxy
import javax.inject.Inject

class AccountsActivity : BaseBackActivity() {

    @Inject
    lateinit var accountController: AccountController

    private lateinit var summaryPresenter: AccountsSummaryPresenter
    private lateinit var binding: ActivityAccountsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccountsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initToolbar()
        initViews()
    }

    private fun initData(): Boolean {
        appComponent.inject(this)
        summaryPresenter = AccountsSummaryPresenter(this)
        return true
    }

    private fun initViews() {
        binding.listView.addHeaderView(summaryPresenter.create())
        binding.listView.setOnItemClickListener { _, _, i, _ -> onAccountClick(i) }
        binding.btnAddAccount.setOnClickListener { addAccount() }

        registerForContextMenu(binding.listView)
        update()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_accounts, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_transfer) {
            makeTransfer()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    fun onAccountClick(position: Int) {
        val account = accountController.readAll()[position - 1]
        startActivityForResult(EditAccountActivity.newIntent(this, account), REQUEST_EDIT_ACCOUNT)
    }

    fun makeTransfer() {
        CrashlyticsProxy.get().logButton("Add Transfer")
        startActivityForResult(Intent(this, TransferActivity::class.java), REQUEST_TRANSFER)
    }

    fun addAccount() {
        CrashlyticsProxy.get().logButton("Add Account")
        val intent = Intent(this, AddAccountActivity::class.java)
        startActivityForResult(intent, REQUEST_ADD_ACCOUNT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_ADD_ACCOUNT -> update()
                REQUEST_TRANSFER, REQUEST_EDIT_ACCOUNT -> {
                    update()
                    setResult(RESULT_OK)
                }
                else -> {}
            }
        }
    }

    private fun update() {
        binding.listView.adapter = AccountAdapter(this, accountController.readAll())
        summaryPresenter.update()
    }

    companion object {
        private const val REQUEST_ADD_ACCOUNT = 1
        private const val REQUEST_TRANSFER = 2
        private const val REQUEST_EDIT_ACCOUNT = 3
    }
}
