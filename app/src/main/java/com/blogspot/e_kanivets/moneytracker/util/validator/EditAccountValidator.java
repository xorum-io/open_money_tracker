package com.blogspot.e_kanivets.moneytracker.util.validator;

import android.content.Context;

import androidx.annotation.NonNull;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.databinding.FragmentEditAccountBinding;
import com.blogspot.e_kanivets.moneytracker.entity.data.Account;

public class EditAccountValidator implements IValidator<Account> {

    @NonNull
    private final Context context;
    private final FragmentEditAccountBinding binding;

    public EditAccountValidator(
            @NonNull Context context,
            @NonNull FragmentEditAccountBinding binding
    ) {
        this.context = context;
        this.binding = binding;
        initTextWatchers();
    }

    @Override
    public boolean validate() {
        String title = binding.etTitle.getText().toString().trim();
        double goal = Double.MAX_VALUE;

        try {
            goal = Double.parseDouble(binding.etGoal.getText().toString().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        boolean valid = true;

        if (title.isEmpty()) {
            binding.tilTitle.setError(context.getString(R.string.field_cant_be_empty));
            valid = false;
        }

        if (goal == Double.MAX_VALUE) {
            binding.tilGoal.setError(context.getString(R.string.field_cant_be_empty));
            goal = 0;
            valid = false;
        }

        if (Math.abs(goal) > MAX_ABS_VALUE) {
            binding.tilGoal.setError(context.getString(R.string.too_rich_or_poor));
            valid = false;
        }

        return valid;
    }

    private void initTextWatchers() {
        binding.etTitle.addTextChangedListener(new ClearErrorTextWatcher(binding.tilTitle));
        binding.etGoal.addTextChangedListener(new ClearErrorTextWatcher(binding.tilGoal));
    }
}
