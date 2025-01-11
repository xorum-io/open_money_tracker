package com.blogspot.e_kanivets.moneytracker.util.validator;

import android.content.Context;
import androidx.annotation.NonNull;
import android.widget.Toast;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.databinding.ActivityTransferBinding;
import com.blogspot.e_kanivets.moneytracker.entity.data.Transfer;

@SuppressWarnings("WeakerAccess")
public class TransferValidator implements IValidator<Transfer> {

    @NonNull
    private final Context context;

    private ActivityTransferBinding binding;

    public TransferValidator(
            @NonNull Context context,
            @NonNull ActivityTransferBinding binding
    ) {
        this.context = context;
        this.binding = binding;
        initTextWatchers();
    }

    @Override
    public boolean validate() {
        boolean valid = true;

        if (!binding.spinnerFrom.isEnabled()) {
            valid = false;
        }

        if (!binding.spinnerTo.isEnabled()) {
            Toast.makeText(context, R.string.one_account_needed, Toast.LENGTH_SHORT).show();
            valid = false;
        }

        double fromAmount = Double.MAX_VALUE;
        try {
            fromAmount = Double.parseDouble(binding.etFromAmount.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (fromAmount == Double.MAX_VALUE) {
            binding.tilFromAmount.setError(context.getString(R.string.field_cant_be_empty));
            fromAmount = 0;
            valid = false;
        }

        if (fromAmount > MAX_ABS_VALUE) {
            binding.tilFromAmount.setError(context.getString(R.string.too_much_for_transfer));
            valid = false;
        }

        double toAmount = Double.MAX_VALUE;
        try {
            toAmount = Double.parseDouble(binding.etToAmount.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (toAmount == Double.MAX_VALUE) {
            binding.tilToAmount.setError(context.getString(R.string.field_cant_be_empty));
            toAmount = 0;
            valid = false;
        }

        if (toAmount > MAX_ABS_VALUE) {
            binding.tilToAmount.setError(context.getString(R.string.too_much_for_transfer));
            valid = false;
        }

        return valid;
    }

    private void initTextWatchers() {
        binding.etFromAmount.addTextChangedListener(new ClearErrorTextWatcher(binding.tilFromAmount));
        binding.etToAmount.addTextChangedListener(new ClearErrorTextWatcher(binding.tilToAmount));
    }
}
