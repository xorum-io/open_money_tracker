package com.blogspot.e_kanivets.moneytracker.util.validator;

import android.content.Context;
import androidx.annotation.NonNull;

import com.blogspot.e_kanivets.moneytracker.databinding.ActivityAddAccountBinding;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.entity.data.Account;

public class AccountValidator implements IValidator<Account> {

    @NonNull
    private final Context context;

    private ActivityAddAccountBinding binding;

    public AccountValidator(
            @NonNull Context context,
            @NonNull ActivityAddAccountBinding binding
    ) {
        this.context = context;
        this.binding = binding;
        initTextWatchers();
    }

    @Override
    public boolean validate() {
        String title = binding.etTitle.getText().toString().trim();
        double initSum = Double.MAX_VALUE;

        try {
            initSum = Double.parseDouble(binding.etInitSum.getText().toString().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        boolean valid = true;

        if (title.isEmpty()) {
            binding.tilTitle.setError(context.getString(R.string.field_cant_be_empty));
            valid = false;
        }

        if (initSum == Double.MAX_VALUE) {
            binding.tilInitSum.setError(context.getString(R.string.field_cant_be_empty));
            initSum = 0;
            valid = false;
        }

        if (Math.abs(initSum) > MAX_ABS_VALUE) {
            binding.tilInitSum.setError(context.getString(R.string.too_rich_or_poor));
            valid = false;
        }

        return valid;
    }

    private void initTextWatchers() {
        binding.etTitle.addTextChangedListener(new ClearErrorTextWatcher(binding.tilTitle));
        binding.etInitSum.addTextChangedListener(new ClearErrorTextWatcher(binding.tilInitSum));
    }
}
