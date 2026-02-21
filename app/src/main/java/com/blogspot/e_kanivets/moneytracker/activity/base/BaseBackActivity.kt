package com.blogspot.e_kanivets.moneytracker.activity.base

import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.blogspot.e_kanivets.moneytracker.R

abstract class BaseBackActivity : BaseActivity() {

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun initToolbar(): Toolbar? {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        return toolbar
    }
}
