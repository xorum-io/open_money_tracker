package com.blogspot.e_kanivets.moneytracker.activity;

import android.text.method.LinkMovementMethod;

import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.databinding.ActivityAboutBinding;

public class AboutActivity extends BaseBackActivity {

    private ActivityAboutBinding binding;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initToolbar();
        initViews();
    }

    private void initViews() {
        binding.tvAbout.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
