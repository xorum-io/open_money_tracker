package com.blogspot.e_kanivets.moneytracker.activity.exchange_rate

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.BaseAdapter
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity
import com.blogspot.e_kanivets.moneytracker.adapter.ExchangeRateAdapter
import com.blogspot.e_kanivets.moneytracker.controller.data.ExchangeRateController
import com.blogspot.e_kanivets.moneytracker.databinding.ActivityExchangeRatesBinding
import com.blogspot.e_kanivets.moneytracker.entity.ExchangeRatePair
import com.blogspot.e_kanivets.moneytracker.util.CrashlyticsProxy
import com.blogspot.e_kanivets.moneytracker.util.ExchangeRatesSummarizer
import java.util.Collections
import javax.inject.Inject

class ExchangeRatesActivity : BaseBackActivity() {

    companion object {
        private const val REQUEST_ADD_EXCHANGE_RATE = 1
    }

    @Inject lateinit var rateController: ExchangeRateController

    private var exchangeRateList: List<ExchangeRatePair> = emptyList()

    private lateinit var binding: ActivityExchangeRatesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExchangeRatesBinding.inflate(layoutInflater)
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
        registerForContextMenu(binding.listView)
        binding.btnAddExchangeRate.setOnClickListener { addExchangeRate() }
        binding.listView.setOnItemClickListener { _, _, position, _ -> 
            addExchangeRateOnBaseOfExisted(position) 
        }

        update()
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_exchange_rate, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo

        return when (item.itemId) {
            R.id.delete -> {
                deleteExchangeRate(info.position)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    fun deleteExchangeRate(position: Int) {
        CrashlyticsProxy.get().logButton("Delete Exchange Rate")
        rateController.deleteExchangeRatePair(exchangeRateList[position])
        update()
        setResult(RESULT_OK)
    }

    fun addExchangeRate() {
        CrashlyticsProxy.get().logButton("Add Exchange Rate")
        val intent = Intent(this, AddExchangeRateActivity::class.java)
        startActivityForResult(intent, REQUEST_ADD_EXCHANGE_RATE)
    }

    fun addExchangeRateOnBaseOfExisted(position: Int) {
        CrashlyticsProxy.get().logButton("Edit Exchange Rate")
        if (position < 0 || position >= exchangeRateList.size) return
        val intent = Intent(this, AddExchangeRateActivity::class.java)
        intent.putExtra(AddExchangeRateActivity.KEY_EXCHANGE_RATE, exchangeRateList[position])
        startActivityForResult(intent, REQUEST_ADD_EXCHANGE_RATE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == REQUEST_ADD_EXCHANGE_RATE) {
            update()
            setResult(RESULT_OK)
        }
    }

    private fun update() {
        exchangeRateList = ExchangeRatesSummarizer(rateController.readAll()).pairedSummaryList
        Collections.reverse(exchangeRateList)

        binding.listView.adapter = ExchangeRateAdapter(this, exchangeRateList)
        (binding.listView.adapter as BaseAdapter).notifyDataSetChanged()
    }
}
