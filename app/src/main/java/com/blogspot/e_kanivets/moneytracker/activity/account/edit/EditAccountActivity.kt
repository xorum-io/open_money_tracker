package com.blogspot.e_kanivets.moneytracker.activity.account.edit

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.activity.account.edit.fragment.AccountOperationsFragment
import com.blogspot.e_kanivets.moneytracker.activity.account.edit.fragment.EditAccountFragment
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity
import com.blogspot.e_kanivets.moneytracker.adapter.GeneralViewPagerAdapter
import com.blogspot.e_kanivets.moneytracker.controller.data.AccountController
import com.blogspot.e_kanivets.moneytracker.entity.data.Account
import com.blogspot.e_kanivets.moneytracker.databinding.ActivityEditAccountBinding
import javax.inject.Inject

class EditAccountActivity : BaseBackActivity() {

    @Inject
    internal lateinit var accountController: AccountController

    private lateinit var account: Account
    private lateinit var binding: ActivityEditAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initToolbar()
        initViews()
    }

    private fun initData(): Boolean {
        appComponent.inject(this@EditAccountActivity)
        val accountFromParcel: Account? = intent.getParcelableExtra(KEY_ACCOUNT)

        return if (accountFromParcel == null) false
        else {
            account = accountFromParcel
            true
        }
    }

    private fun initViews() {
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        val adapter = GeneralViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(EditAccountFragment.newInstance(account), getString(R.string.information))
        adapter.addFragment(AccountOperationsFragment.newInstance(account), getString(R.string.operations))
        binding.viewPager.adapter = adapter

        binding.viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    binding.fabDone.show()
                    showKeyboard()
                } else {
                    binding.fabDone.hide()
                    hideKeyboard()
                }
            }
        })
    }

    private fun hideKeyboard() {
        val view: View? = currentFocus
        if (view != null) {
            val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun showKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.root.findViewById(R.id.etTitle), 0)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(if (account.isArchived) R.menu.menu_archived_account else R.menu.menu_account, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_archive -> archive()
            R.id.action_restore -> restore()
            R.id.action_delete -> delete()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun archive(): Boolean {
        if (account == accountController.readDefaultAccount()) {
            showToast(R.string.cant_archive_default_account)
        } else {
            accountController.archive(account)
            setResult(Activity.RESULT_OK)
            finish()
        }

        return true
    }

    private fun restore(): Boolean {
        accountController.restore(account)
        setResult(Activity.RESULT_OK)
        finish()

        return true
    }

    private fun delete(): Boolean {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.delete_account_title)
        builder.setMessage(R.string.delete_account_message)
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            accountController.delete(account)
            setResult(Activity.RESULT_OK)
            finish()
        }
        builder.setNegativeButton(android.R.string.cancel, null)
        builder.show()

        return true
    }

    companion object {

        private const val KEY_ACCOUNT = "key_account"

        fun newIntent(context: Context, account: Account): Intent {
            val intent = Intent(context, EditAccountActivity::class.java)
            intent.putExtra(KEY_ACCOUNT, account)
            return intent
        }
    }
}
