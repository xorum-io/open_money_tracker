package com.blogspot.e_kanivets.moneytracker.activity

import android.text.method.LinkMovementMethod
import android.os.Bundle
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity
import com.blogspot.e_kanivets.moneytracker.databinding.ActivityAboutBinding

class AboutActivity : BaseBackActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
        initViews()
    }

    private fun initViews() {
        binding.tvAbout.movementMethod = LinkMovementMethod.getInstance()
    }
}
