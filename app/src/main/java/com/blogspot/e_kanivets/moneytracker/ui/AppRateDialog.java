package com.blogspot.e_kanivets.moneytracker.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.MtApp;
import com.blogspot.e_kanivets.moneytracker.controller.PreferenceController;
import com.blogspot.e_kanivets.moneytracker.databinding.DialogRateBinding;

import javax.inject.Inject;

public class AppRateDialog extends AlertDialog {
    private static final String GP_MARKET = "market://details?id=";

    private Context context;
    private DialogRateBinding binding;

    @Inject
    PreferenceController preferenceController;

    public AppRateDialog(Context context) {
        super(context);
        this.context = context;
        MtApp.get().getAppComponent().inject(AppRateDialog.this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DialogRateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.yesButton.setOnClickListener(view -> yes());
        binding.maybeButton.setOnClickListener(view -> dismiss());
        binding.thanksButton.setOnClickListener(view -> thanks());
    }

    public void yes() {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GP_MARKET + context.getPackageName())));
        preferenceController.appRated();
        dismiss();
    }

    public void thanks() {
        preferenceController.appRated();
        dismiss();
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
