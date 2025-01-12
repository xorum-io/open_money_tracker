package com.blogspot.e_kanivets.moneytracker.util.validator;

import android.content.Context;
import androidx.annotation.NonNull;
import android.widget.Toast;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.databinding.ActivityAddRecordBinding;
import com.blogspot.e_kanivets.moneytracker.entity.data.Record;

public class RecordValidator implements IValidator<Record> {

    @NonNull
    private final Context context;

    private final ActivityAddRecordBinding binding;

    public RecordValidator(
            @NonNull Context context,
            @NonNull ActivityAddRecordBinding binding
    ) {
        this.context = context;
        this.binding = binding;

        initTextWatchers();
    }

    @Override
    public boolean validate() {
        boolean valid = true;

        String category = binding.etCategory.getText().toString().trim();

        if (category.isEmpty()) {
            binding.tilCategory.setError(context.getString(R.string.field_cant_be_empty));
            valid = false;
        }

        //Check if price is valid
        double price = Double.MAX_VALUE;
        try {
            price = Double.parseDouble(binding.etPrice.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (price == Double.MAX_VALUE) {
            binding.tilPrice.setError(context.getString(R.string.field_cant_be_empty));
            price = 0;
            valid = false;
        }

        if (price > MAX_ABS_VALUE) {
            binding.tilPrice.setError(context.getString(R.string.too_rich));
            valid = false;
        }

        if (!binding.spinnerAccount.isEnabled()) {
            Toast.makeText(context, R.string.one_account_needed, Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

    private void initTextWatchers() {
        binding.etPrice.addTextChangedListener(new ClearErrorTextWatcher(binding.tilPrice));
        binding.etTitle.addTextChangedListener(new ClearErrorTextWatcher(binding.tilTitle));
        binding.etCategory.addTextChangedListener(new ClearErrorTextWatcher(binding.tilCategory));
    }
}
