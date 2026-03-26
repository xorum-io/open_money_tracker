package com.blogspot.e_kanivets.moneytracker.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.blogspot.e_kanivets.moneytracker.controller.PreferenceController
import com.blogspot.e_kanivets.moneytracker.databinding.DialogRateBinding
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AppRateDialog(context: Context) : AlertDialog(context), KoinComponent {

    private lateinit var binding: DialogRateBinding

    private val preferenceController: PreferenceController by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogRateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.yesButton.setOnClickListener { yes() }
        binding.maybeButton.setOnClickListener { dismiss() }
        binding.thanksButton.setOnClickListener { thanks() }
    }

    fun yes() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(GP_MARKET + context.packageName))
        context.startActivity(intent)
        preferenceController.appRated()
        dismiss()
    }

    fun thanks() {
        preferenceController.appRated()
        dismiss()
    }

    override fun dismiss() {
        try {
            super.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val GP_MARKET = "market://details?id="
    }
} 