package com.blogspot.e_kanivets.moneytracker.util.validator;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.widget.AppCompatSpinner;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.entity.data.Transfer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Util class for Transfer validation.
 * Created on 07.12.2016.
 *
 * @author Evgenii Kanivets
 */

@SuppressWarnings("WeakerAccess")
public class TransferValidator implements IValidator<Transfer> {

    @NonNull
    private final Context context;

    @BindView(R.id.spinner_from)
    AppCompatSpinner spinnerFrom;
    @BindView(R.id.spinner_to)
    AppCompatSpinner spinnerTo;
    @BindView(R.id.til_from_amount)
    TextInputLayout tilFromAmount;
    @BindView(R.id.et_from_amount)
    EditText etFromAmount;
    @BindView(R.id.til_to_amount)
    TextInputLayout tilToAmount;
    @BindView(R.id.et_to_amount)
    EditText etToAmount;

    public TransferValidator(@NonNull Context context, @NonNull View view) {
        this.context = context;
        ButterKnife.bind(this, view);
        initTextWatchers();
    }

    @Override
    public boolean validate() {
        boolean valid = true;

        if (!spinnerFrom.isEnabled()) {
            valid = false;
        }

        if (!spinnerTo.isEnabled()) {
            Toast.makeText(context, R.string.one_account_needed, Toast.LENGTH_SHORT).show();
            valid = false;
        }

        double fromAmount = Double.MAX_VALUE;
        try {
            fromAmount = Double.parseDouble(etFromAmount.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (fromAmount == Double.MAX_VALUE) {
            tilFromAmount.setError(context.getString(R.string.field_cant_be_empty));
            fromAmount = 0;
            valid = false;
        }

        if (fromAmount > MAX_ABS_VALUE) {
            tilFromAmount.setError(context.getString(R.string.too_much_for_transfer));
            valid = false;
        }

        double toAmount = Double.MAX_VALUE;
        try {
            toAmount = Double.parseDouble(etToAmount.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (toAmount == Double.MAX_VALUE) {
            tilToAmount.setError(context.getString(R.string.field_cant_be_empty));
            toAmount = 0;
            valid = false;
        }

        if (toAmount > MAX_ABS_VALUE) {
            tilToAmount.setError(context.getString(R.string.too_much_for_transfer));
            valid = false;
        }

        return valid;
    }

    private void initTextWatchers() {
        etFromAmount.addTextChangedListener(new ClearErrorTextWatcher(tilFromAmount));
        etToAmount.addTextChangedListener(new ClearErrorTextWatcher(tilToAmount));
    }
}
