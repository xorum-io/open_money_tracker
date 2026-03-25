package com.blogspot.e_kanivets.moneytracker.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.blogspot.e_kanivets.moneytracker.MtApp
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.controller.FormatController
import com.blogspot.e_kanivets.moneytracker.databinding.ViewAccountBinding
import com.blogspot.e_kanivets.moneytracker.entity.data.Account
import javax.inject.Inject

class AccountAdapter(
    private val context: Context,
    private val accounts: List<Account>
) : BaseAdapter() {
    @Inject
    lateinit var formatController: FormatController

    private val whiteRed: Int = context.resources.getColor(R.color.white_red)
    private val whiteGreen: Int = context.resources.getColor(R.color.white_green)
    private val red: Int = context.resources.getColor(R.color.red)
    private val green: Int = context.resources.getColor(R.color.green)
    private val grey: Int = context.resources.getColor(R.color.grey_inactive)

    init {
        MtApp.instance.appComponent.inject(this)
    }

    override fun getCount(): Int = accounts.size

    override fun getItem(position: Int): Any = accounts[position]

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ViewAccountBinding
        val view: View
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(context)
            binding = ViewAccountBinding.inflate(layoutInflater, parent, false)
            view = binding.root
            view.tag = binding
        } else {
            binding = convertView.tag as ViewAccountBinding
            view = convertView
        }

        val account = accounts[position]

        if (account.isArchived) {
            view.setBackgroundColor(grey)
        } else {
            view.setBackgroundColor(if (account.fullSum >= 0.0) whiteGreen else whiteRed)
        }

        binding.tvCurSum.setTextColor(if (account.fullSum >= 0.0) green else red)
        binding.tvCurrency.setTextColor(if (account.fullSum >= 0.0) green else red)

        binding.tvTitle.text = account.title
        binding.tvCurSum.text = formatController.formatSignedAmount(account.fullSum)
        binding.tvCurrency.text = account.currency

        return view
    }
} 